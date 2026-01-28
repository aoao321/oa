package com.aoao.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aoao.context.UserContext;
import com.aoao.dto.process.ProcessFormDto;
import com.aoao.dto.process.ProcessQueryDto;
import com.aoao.mapper.*;
import com.aoao.model.process.Process;
import com.aoao.model.process.ProcessRecord;
import com.aoao.model.process.ProcessTemplate;
import com.aoao.model.process.ProcessType;
import com.aoao.model.system.SysUser;
import com.aoao.result.PageResult;
import com.aoao.service.ProcessRecordService;
import com.aoao.service.ProcessService;
import com.aoao.vo.process.ApprovalVo;
import com.aoao.vo.process.ProcessTypeWithTemplateVo;
import com.aoao.vo.process.ProcessVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

/**
 * @author aoao
 * @create 2025-07-18-21:07
 */
@Slf4j
@Service
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private ProcessMapper processMapper;
    @Autowired
    private ProcessTypeMapper processTypeMapper;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ProcessTemplateMapper processTemplateMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProcessRecordService processRecordService;
    @Autowired
    private ProcessRecordMapper processRecordMapper;
    @Autowired
    private HistoryService historyService;

    @Override
    public PageResult<ProcessVo> page(int page, int limit, ProcessQueryDto processQueryDto) {
        PageHelper.startPage(page, limit);
        List<ProcessVo> processVos = processMapper.selectProcessVo(processQueryDto);
        PageInfo<ProcessVo> pageInfo = new PageInfo<>(processVos);

        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public void deloyByZip(String path) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(path);
        ZipInputStream zipInputStream = new ZipInputStream(is);
        // 部署流程
        repositoryService.createDeployment().addZipInputStream(zipInputStream).deploy();
    }

    @Override
    public List<ProcessTypeWithTemplateVo> findProcessTypeWithTemplate() {
        // 1. 查询所有流程分类
        List<ProcessType> processTypeList = processTypeMapper.selectList(null);

        // 2. 收集所有类型id
        List<Long> typeIds = processTypeList.stream()
                .map(ProcessType::getId)
                .collect(Collectors.toList());

        if (typeIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. 查询模板，直接返回实体类列表
        List<ProcessTemplate> templateList = processTemplateMapper.selectListByTypeIds(typeIds);

        // 4. 按类型ID分组模板
        Map<Long, List<ProcessTemplate>> templateGroup = templateList.stream()
                .collect(Collectors.groupingBy(ProcessTemplate::getProcessTypeId));

        // 5. 组装VO
        List<ProcessTypeWithTemplateVo> voList = new ArrayList<>();
        for (ProcessType processType : processTypeList) {
            ProcessTypeWithTemplateVo vo = new ProcessTypeWithTemplateVo();
            BeanUtils.copyProperties(processType, vo);
            vo.setProcessTemplateList(templateGroup.getOrDefault(processType.getId(), Collections.emptyList()));
            voList.add(vo);
        }

        return voList;
    }

    @Override
    public void startUp(ProcessFormDto processFormDto, HttpServletRequest request) {
        // 获取用户名
        String username = UserContext.getUsername();
        SysUser user = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username", username));
        // 根据模板id查询到模板信息
        Long templateId = processFormDto.getProcessTemplateId();
        Long typeId = processFormDto.getProcessTypeId();
        ProcessTemplate template = processTemplateMapper.selectById(templateId);
        // 记录提交信息到process表中
        String formValues = processFormDto.getFormValues();
        Process process = new Process();
        process.setProcessTemplateId(templateId);
        process.setUserId(user.getId());
        process.setProcessTypeId(typeId);
        process.setTitle(username + "发起" + template.getName());
        process.setStatus(1);  // 审批中
        process.setProcessCode(String.valueOf(System.currentTimeMillis()));
        process.setFormValues(formValues);
        processMapper.insert(process);
        // 启动流程实例
        // 传入流程定义key，业务key，参数map
        String processDefinitionKey = template.getProcessDefinitionKey();
        // 主键回显，定义业务id
        Long processId = process.getId();
        // 将表单参数转化为map
        JSONObject jsonObject = JSON.parseObject(formValues);
        JSONObject formData = jsonObject.getJSONObject("formData");
        // 流程参数
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        // 启动
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(processDefinitionKey, String.valueOf(processId), map);
        String instanceId = processInstance.getId();

        // 查询下一个审批人
        List<Task> taskList = taskService
                .createTaskQuery()
                .processInstanceId(instanceId)
                .list();
        List<String> assigneeList = new ArrayList<>();
        for (Task task : taskList) {
            String assigneeName = task.getAssignee();
            // 查询审批人信息
            SysUser assignee = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username", assigneeName));
            String name = assignee.getName();
            assigneeList.add(name);

            // TODO:推送消息
        }

        // 关联业务和流程，更新process表
        process.setProcessInstanceId(instanceId);
        process.setDescription("等待" + StringUtils.join(assigneeList.toArray(), ",") + "审批");
        // 设置当前审批人字段
        process.setCurrentAuditor(StringUtils.join(assigneeList.toArray(), ","));
        processMapper.updateById(process);

        // 插入审批记录
        processRecordService.record(processId, 1, "发起申请");
    }

    /**
     * 查询负责人待办任务
     *
     * @param page
     * @param limit
     * @return
     */
    @Override
    public PageResult<ProcessVo> findPending(int page, int limit) {
        String username = UserContext.getUsername();
        SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username", username));
        // 封装查询的条件，根据当前登录用户名称
        TaskQuery query = taskService.createTaskQuery()
                .taskAssignee(username)
                .orderByTaskCreateTime()
                .desc();
        int offset = (page - 1) * limit;
        List<Task> taskList = query.listPage(offset, limit);
        if (taskList == null || taskList.size() == 0) {
            return new PageResult<>(0, Collections.emptyList());
        }
        // 分页查询，所有待办集合
        List<Long> processIds = new ArrayList<>();
        for (Task task : taskList) {
            // 获取流程实例id
            String processInstanceId = task.getProcessInstanceId();
            // 获取实例对象
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            // 获取processId
            String businessKey = processInstance.getBusinessKey();
            if (businessKey == null) {
                continue;
            }
            long processId = Long.parseLong(businessKey);
            processIds.add(processId);
        }

        // 查询所有的待办任务
        PageHelper.startPage(page, limit);
        // 当前审批人包括当前用户名
        List<ProcessVo> processVoList = processMapper.selectProcessVoByProcessIds(processIds, sysUser.getName());
        PageInfo<ProcessVo> pageInfo = new PageInfo<>(processVoList);
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public Map<String, Object> show(Long id) {
        String username = UserContext.getUsername();
        // 根据流程id获取Process
        Process process = processMapper.selectById(id);
        // 获取以登记信息
        List<ProcessRecord> processRecords = processRecordMapper
                .selectList(new QueryWrapper<ProcessRecord>()
                        .eq("process_id", process.getId()));
        // 根据模板id查询模板信息
        ProcessTemplate processTemplate = processTemplateMapper.selectById(process.getProcessTemplateId());
        // 判断当前用户是否可以审批
        boolean isApprove = false;
        List<Task> taskList = taskService
                .createTaskQuery()
                .processInstanceId(process.getProcessInstanceId())
                .list();
        for (Task task : taskList) {
            String assignee = task.getAssignee();
            // 判断审批人是否是当前用户
            if (assignee.equals(username)) {
                isApprove = true;
            }
        }
        // 封装数据
        Map<String, Object> map = new HashMap<>();
        map.put("isApprove", isApprove);
        map.put("process", process);
        map.put("processTemplate", processTemplate);
        map.put("processRecordList", processRecords);

        return map;
    }

    @Override
    public void approve(ApprovalVo approvalVo) {
        String taskId = approvalVo.getTaskId();
        // 获取当前任务的流程变量（可用于调试或判断）
        Map<String, Object> variable = taskService.getVariables(taskId);
        for (Map.Entry<String, Object> entry : variable.entrySet()) {
            // 可选：打印或后续逻辑使用
            System.out.println("流程变量：" + entry.getKey() + " = " + entry.getValue());
        }

        // 查询当前流程对象
        Process process = processMapper.selectById(approvalVo.getProcessId());

        if (approvalVo.getStatus() == 1) {
            //  1. 审批通过：完成当前任务
            taskService.complete(taskId);

            // 2. 记录审批日志
            processRecordService.record(approvalVo.getProcessId(), approvalVo.getStatus(), "通过");

            //  3. 查询是否还有后续审批人（多个并行任务）
            List<Task> taskList = taskService
                    .createTaskQuery()
                    .processInstanceId(process.getProcessInstanceId())
                    .list();

            //  4. 汇总下一个审批人的信息
            if (!taskList.isEmpty()) {
                List<String> assigneeList = new ArrayList<>();
                for (Task task : taskList) {
                    String assignee = task.getAssignee();
                    if (StringUtils.isNotBlank(assignee)) {
                        SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>()
                                .eq("username", assignee));
                        if (sysUser != null) {
                            assigneeList.add(sysUser.getName());
                            // TODO: 可以加推送通知
                        }
                    }
                }

                //  5. 更新流程状态：等待下一批审批人
                process.setDescription("等待 " + StringUtils.join(assigneeList, ", ") + " 审批");
                process.setStatus(1); // 审批中
            } else { // taskList为空 所有任务完成，流程已结束
                process.setDescription("审批完成");
                process.setStatus(2); // 审批完成
            }
            processMapper.updateById(process);

        } else {// 审核不通过
            // 获取任务对象
            Task task = taskService.createTaskQuery().taskId(approvalVo.getTaskId()).singleResult();

            // 获取流程定义
            BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
            // 获取结束节点
            // 获取主流程的流程元素
            Collection<FlowElement> flowElements = bpmnModel.getMainProcess().getFlowElements();

            List<EndEvent> endEvents = flowElements.stream()
                    .filter(e -> e instanceof EndEvent)
                    .map(e -> (EndEvent) e)
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(endEvents)) {
                return;
            }
            FlowNode endEvent = endEvents.get(0);
            // 当前流向节点
            FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getProcessDefinitionId());
            // 清除节点
            currentFlowNode.getOutgoingFlows().clear();
            // 指定下一个节点为endEvent
            SequenceFlow newSequenceFlow = new SequenceFlow();
            newSequenceFlow.setId("newSequenceFlow");
            newSequenceFlow.setSourceFlowElement(currentFlowNode);
            newSequenceFlow.setTargetFlowElement(endEvent);
            // 当前节点指向新方向
            List newSequenceFlowList = new ArrayList();
            newSequenceFlowList.add(newSequenceFlow);
            currentFlowNode.setOutgoingFlows(newSequenceFlowList);
            // 完成当前任务
            taskService.complete(taskId);
            //  更新流程状态
            process.setDescription("审批不通过");
            process.setStatus(-1); // 自定义的不通过状态码
            processMapper.updateById(process);

            // 记录审批行为
            processRecordService.record(approvalVo.getProcessId(), approvalVo.getStatus(), "不通过");
        }
    }

    @Override
    public PageResult<ProcessVo> findProcessed(int page, int limit) {
        String username = UserContext.getUsername();
        // 1. 查询该用户完成的所有任务
        List<HistoricTaskInstance> taskList = historyService
                .createHistoricTaskInstanceQuery()
                .taskAssignee(username)
                .finished()
                .orderByHistoricTaskInstanceEndTime().desc()
                .list();
        // 2. 取出流程业务id
        // 2.1. 获取流程实例 ID 列表（去重）
        List<String> processInstanceIdList = taskList.stream()
                .map(HistoricTaskInstance::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());

        // 2.2. 遍历每个流程实例，获取 businessKey
        List<String> businessKeyList = new ArrayList<>();
        for (String processInstanceId : processInstanceIdList) {
            HistoricProcessInstance historicProcessInstance = historyService
                    .createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            if (historicProcessInstance != null && historicProcessInstance.getBusinessKey() != null) {
                businessKeyList.add(historicProcessInstance.getBusinessKey());
            }
        }
        List<Long> processIds = businessKeyList
                .stream()
                .map(key -> Long.parseLong(key))
                .collect(Collectors.toList());
        // 3. 根据流程实例id查 oa_process 表
        PageHelper.startPage(page, limit);
        // 当前审批人包括当前用户名
        List<ProcessVo> processVoList = processMapper.selectProcessVoByProcessIds(processIds, null);
        PageInfo<ProcessVo> pageInfo = new PageInfo<>(processVoList);

        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public PageResult<ProcessVo> findStarted(int page, int limit) {
        // 根据当前用户查询vo
        String username = UserContext.getUsername();
        SysUser sysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username", username));
        ProcessQueryDto processQueryDto = new ProcessQueryDto();
        processQueryDto.setUserId(sysUser.getId());
        // 查询vo
        PageHelper.startPage(page, limit);
        List<ProcessVo> processVoList = processMapper.selectProcessVo(processQueryDto);
        PageInfo<ProcessVo> pageInfo = new PageInfo<>(processVoList);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }
}


