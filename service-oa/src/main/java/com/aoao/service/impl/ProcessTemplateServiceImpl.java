package com.aoao.service.impl;

import com.aoao.dto.process.ProcessTemplateDto;
import com.aoao.dto.process.ProcessTypeDto;
import com.aoao.mapper.ProcessTemplateMapper;
import com.aoao.model.process.ProcessTemplate;
import com.aoao.model.process.ProcessType;
import com.aoao.result.PageResult;
import com.aoao.service.ProcessTemplateService;
import com.aoao.vo.process.ProcessTemplateQueryListVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aoao
 * @create 2025-07-18-9:22
 */
@Service
public class ProcessTemplateServiceImpl implements ProcessTemplateService {

    @Autowired
    private ProcessTemplateMapper processTemplateMapper;

    @Override
    public PageResult<ProcessTemplateQueryListVo> list(int page, int limit) {
        PageHelper.startPage(page, limit);
        List<ProcessTemplateQueryListVo> processTemplates = processTemplateMapper.selectListWithTypeName();
        PageInfo<ProcessTemplateQueryListVo> pageInfo = new PageInfo<>(processTemplates);
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public void save(ProcessTemplate processTemplate) {
        processTemplateMapper.insert(processTemplate);
    }


    @Override
    public void removeById(Long id) {
        processTemplateMapper.deleteById(id);
    }

    @Override
    public ProcessTemplate getById(Long id) {
        return processTemplateMapper.selectById(id);
    }

    @Override
    public void update(ProcessTemplate processTemplate) {
        processTemplateMapper.updateById(processTemplate);
    }


    @Override
    public Map<String,Object> uploadProcessDefinition(MultipartFile file) throws FileNotFoundException {
        // 获取classes
        String path = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath();
        // 设置上传文件夹
        File tempFile = new File(path + "/process/");
        // 不存在创建文件夹
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        String filename = file.getOriginalFilename();
        File imageFile = new File(path + "/process/" + filename);
        // 保存文件流到本地
        try{
            file.transferTo(imageFile);
        }catch (IOException e){
            throw new FileNotFoundException("文件上传失败");
        }
        HashMap<String, Object> map = new HashMap<>();

        // 返回文件的路径，以文件名前缀为模板key
        map.put("processDefinitionPath", "processes/" + filename);
        map.put("processDefinitionKey", filename.substring(0, filename.lastIndexOf(".")));
        return map;
    }

    @Override
    public void publish(Long id) {
        ProcessTemplate processTemplate = processTemplateMapper.selectById(id);
        // 设置status=1
        processTemplate.setStatus(1);
        processTemplateMapper.updateById(processTemplate);

        // 部署流程
    }
}
