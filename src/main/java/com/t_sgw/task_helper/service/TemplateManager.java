package com.t_sgw.task_helper.service;

import com.t_sgw.task_helper.dto.TaskDTO;
import com.t_sgw.task_helper.entity.Task;
import com.t_sgw.task_helper.entity.Template;
import com.t_sgw.task_helper.repository.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TemplateManager {

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private TaskService taskService;

    /**
     * テンプレートを作成
     * 
     * @param name テンプレート名
     * @param dto  タスクDTO
     * @return 作成されたテンプレート
     */
    public Template createTemplate(String name, TaskDTO dto) {
        Template template = new Template();
        template.setName(name);
        template.setTaskTitle(dto.getTitle());
        template.setTaskDescription(dto.getDescription());
        template.setTaskDueDate(dto.getDueDate());
        template.setTaskCategory(dto.getCategory());
        template.setTaskPriority(dto.getPriority());

        return templateRepository.save(template);
    }

    /**
     * テンプレートを編集
     * 
     * @param id  テンプレートID
     * @param dto タスクDTO
     * @return 更新されたテンプレート
     * @throws RuntimeException テンプレートが見つからない場合
     */
    public Template editTemplate(Long id, TaskDTO dto) {
        Optional<Template> optionalTemplate = templateRepository.findById(id);
        if (optionalTemplate.isPresent()) {
            Template template = optionalTemplate.get();
            template.setTaskTitle(dto.getTitle());
            template.setTaskDescription(dto.getDescription());
            template.setTaskDueDate(dto.getDueDate());
            template.setTaskCategory(dto.getCategory());
            template.setTaskPriority(dto.getPriority());

            return templateRepository.save(template);
        } else {
            throw new RuntimeException("Template not found with id: " + id);
        }
    }

    /**
     * テンプレートを削除
     * 
     * @param id テンプレートID
     * @throws RuntimeException テンプレートが見つからない場合
     */
    public void deleteTemplate(Long id) {
        if (templateRepository.existsById(id)) {
            templateRepository.deleteById(id);
        } else {
            throw new RuntimeException("Template not found with id: " + id);
        }
    }

    /**
     * テンプレートからタスクを作成
     * 
     * @param id テンプレートID
     * @return 作成されたタスク
     * @throws RuntimeException テンプレートが見つからない場合
     */
    public Task applyTemplate(Long id) {
        Optional<Template> optionalTemplate = templateRepository.findById(id);
        if (optionalTemplate.isPresent()) {
            Template template = optionalTemplate.get();

            TaskDTO dto = new TaskDTO();
            dto.setTitle(template.getTaskTitle());
            dto.setDescription(template.getTaskDescription());
            dto.setDueDate(template.getTaskDueDate());
            dto.setCompleted(false); // 新規タスクは未完了
            dto.setCategory(template.getTaskCategory());
            dto.setPriority(template.getTaskPriority());

            return taskService.createTask(dto);
        } else {
            throw new RuntimeException("Template not found with id: " + id);
        }
    }

    /**
     * 指定IDのテンプレートを取得
     * 
     * @param id テンプレートID
     * @return テンプレート
     * @throws RuntimeException テンプレートが見つからない場合
     */
    public Template getTemplate(Long id) {
        Optional<Template> optionalTemplate = templateRepository.findById(id);
        if (optionalTemplate.isPresent()) {
            return optionalTemplate.get();
        } else {
            throw new RuntimeException("Template not found with id: " + id);
        }
    }

    /**
     * 全テンプレートを取得
     * 
     * @return 全テンプレートのリスト
     */
    public List<Template> getAllTemplates() {
        return templateRepository.findAllByOrderByNameAsc();
    }

    /**
     * テンプレート名で検索
     * 
     * @param name テンプレート名
     * @return 指定した名前のテンプレートのリスト
     */
    public List<Template> getTemplatesByName(String name) {
        return templateRepository.findByName(name);
    }

    /**
     * キーワードでテンプレートを検索
     * 
     * @param keyword 検索キーワード
     * @return 検索条件に一致するテンプレートのリスト
     */
    public List<Template> searchTemplates(String keyword) {
        return templateRepository.findByNameContaining(keyword);
    }

    /**
     * TemplateをTaskDTOに変換
     * 
     * @param template テンプレートエンティティ
     * @return タスクDTO
     */
    public TaskDTO convertToTaskDTO(Template template) {
        TaskDTO dto = new TaskDTO();
        dto.setTitle(template.getTaskTitle());
        dto.setDescription(template.getTaskDescription());
        dto.setDueDate(template.getTaskDueDate());
        dto.setCompleted(false);
        dto.setCategory(template.getTaskCategory());
        dto.setPriority(template.getTaskPriority());
        return dto;
    }
}
