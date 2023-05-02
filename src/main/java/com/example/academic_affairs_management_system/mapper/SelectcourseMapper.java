package com.example.academic_affairs_management_system.mapper;

import com.example.academic_affairs_management_system.entity.Selectcourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author losewinner
 * @since 2023-04-27
 */
@Mapper
public interface SelectcourseMapper extends BaseMapper<Selectcourse> {
    @Select("select studentId,semester,courseId,staffId,classTime,testScore,signScore,homeworkScore,score from selectcourse " +
            "where semester=#{semester} and courseId=#{courseId} and staffId=#{staffId} and classTime=#{classTime}")
    List<Selectcourse> getAllInfo(String semester,String courseId,String staffId,String classTime);

    public List<Selectcourse> selectAll();

    //统计分数,按照课程来统计，不需要搜学生和老师，但是学生的姓名和学号还是要查的
    @Select("SELECT" +
            "selectcourse.semester," +
            "selectcourse.studentId,student.name," +
            "selectcourse.courseId,course.name," +
            "testScore" +
            "FROM selectcourse,course,student" +
            "WHERE" +
            "selectcourse.courseId = course.courseId" +
            "AND student.studentId = selectcourse.studentId" +
            "AND semester = #{semester} " +
            "AND selectcourse.courseId = #{courseId} " +
            "AND course.name = #{courseName}")
    List<Selectcourse> getCourseScore(String semester,String courseId,String courseName);
    //统计分数，按照学生来统计，展示的是一个学生（或者同名学生）的所有一个学期的课程总成绩
    @Select("SELECT" +
            "selectcourse.semester," +
            "selectcourse.studentId,student.name," +
            "selectcourse.courseId,course.name," +
            "testScore" +
            "FROM selectcourse,course,student" +
            "WHERE" +
            "selectcourse.courseId = course.courseId" +
            "AND student.studentId = selectcourse.studentId" +
            "AND semester = #{semester} " +
            "AND selectcourse.studentId = #{studentId} " +
            "AND student.name = #{studentName} ")
    List<Selectcourse> getStudentScore(String semester, String studentId,String studentName);
}
