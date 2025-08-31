package com.t_sgw.task_helper.repository;

import com.t_sgw.task_helper.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * 指定した日時より前の期限のタスクを取得
     * 
     * @param date 基準日時
     * @return 期限が近いタスクのリスト
     */
    List<Task> findByDueDateBefore(LocalDateTime date);

    /**
     * 完了状態でフィルタリングしてタスクを取得
     * 
     * @param completed 完了状態
     * @return 指定した完了状態のタスクのリスト
     */
    List<Task> findByCompleted(boolean completed);

    /**
     * カテゴリでフィルタリングしてタスクを取得
     * 
     * @param category カテゴリ
     * @return 指定したカテゴリのタスクのリスト
     */
    List<Task> findByCategory(String category);

    /**
     * 優先度でフィルタリングしてタスクを取得
     * 
     * @param priority 優先度
     * @return 指定した優先度のタスクのリスト
     */
    List<Task> findByPriority(String priority);

    /**
     * タイトルまたは説明に指定したキーワードが含まれるタスクを検索
     * 
     * @param keyword 検索キーワード
     * @return 検索条件に一致するタスクのリスト
     */
    @Query("SELECT t FROM Task t WHERE t.title LIKE %:keyword% OR t.description LIKE %:keyword%")
    List<Task> findByTitleOrDescriptionContaining(@Param("keyword") String keyword);

    /**
     * 期限が近い未完了のタスクを取得（通知用）
     * 
     * @param date 基準日時
     * @return 期限が近い未完了のタスクのリスト
     */
    @Query("SELECT t FROM Task t WHERE t.dueDate <= :date AND t.completed = false ORDER BY t.dueDate ASC")
    List<Task> findUpcomingIncompleteTasks(@Param("date") LocalDateTime date);

    /**
     * 作成日時の降順でタスクを取得
     * 
     * @return 作成日時の降順でソートされたタスクのリスト
     */
    List<Task> findAllByOrderByCreatedAtDesc();

    /**
     * 期限日時の昇順でタスクを取得
     * 
     * @return 期限日時の昇順でソートされたタスクのリスト
     */
    List<Task> findAllByOrderByDueDateAsc();
}
