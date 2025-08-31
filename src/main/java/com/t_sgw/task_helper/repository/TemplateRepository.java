package com.t_sgw.task_helper.repository;

import com.t_sgw.task_helper.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    /**
     * テンプレート名で検索
     * 
     * @param name テンプレート名
     * @return 指定した名前のテンプレートのリスト
     */
    List<Template> findByName(String name);

    /**
     * テンプレート名に指定したキーワードが含まれるテンプレートを検索
     * 
     * @param keyword 検索キーワード
     * @return 検索条件に一致するテンプレートのリスト
     */
    List<Template> findByNameContaining(String keyword);

    /**
     * 作成日時の降順でテンプレートを取得
     * 
     * @return 作成日時の降順でソートされたテンプレートのリスト
     */
    List<Template> findAllByOrderByCreatedAtDesc();

    /**
     * テンプレート名の昇順でテンプレートを取得
     * 
     * @return テンプレート名の昇順でソートされたテンプレートのリスト
     */
    List<Template> findAllByOrderByNameAsc();
}
