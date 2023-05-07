package com.example.academic_affairs_management_system.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.academic_affairs_management_system.common.QueryPageParam;
import com.example.academic_affairs_management_system.common.Result;
import com.example.academic_affairs_management_system.controller.dto.AdminPack.Score;
import com.example.academic_affairs_management_system.controller.dto.AdminPack.delScore;
import com.example.academic_affairs_management_system.controller.dto.TeacherPack.Student;
import com.example.academic_affairs_management_system.entity.Selectcourse;
import com.example.academic_affairs_management_system.mapper.SelectcourseMapper;
import com.example.academic_affairs_management_system.service.ISelectcourseService;

import com.example.academic_affairs_management_system.service.ISemestatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author losewinner
 * @since 2023-04-27
 */
@RestController
@RequestMapping("/selectcourse")
public class SelectcourseController {
    @Autowired
    private ISelectcourseService iSelectcourseService;

    @Autowired
    private ISemestatusService iSemestatusService;

    @Resource
    private SelectcourseMapper selectcourseMapper;

    @GetMapping("/allstudent")
    public Result findPage(@RequestParam String semester,
                           @RequestParam String courseId,
                           @RequestParam String staffId,
                           @RequestParam String classTime ){
        return Result.success(iSelectcourseService.getAllInfo(semester,courseId,staffId,classTime));
    }
    @GetMapping("/list")
    public List<Selectcourse> selectAll(){
        return iSelectcourseService.selectAll();
    }
    @PostMapping("/modifyscore")
    public Result modify_sorce(Selectcourse selectcourse){
        return Result.success(iSelectcourseService.saveOrUpdate(selectcourse));
    }

    @PostMapping("/getstudent")
    public Result select_stu(@RequestBody QueryPageParam queryPageParam) {
        /*
         * 查询某班级所有学生
         */
        int pagenum = queryPageParam.getPagenum(),pagesize=queryPageParam.getPagesize();
        HashMap param= queryPageParam.getParam();
        String semester =  iSemestatusService.getnowsemester();
        System.out.println(param);
        System.out.println(pagenum);
        System.out.println(pagesize);
        param.get("courseid").toString();
        param.get("staffid").toString();
        param.get("classtime").toString();

        List<Student> data = iSelectcourseService.select_stu(pagenum,pagesize,
                semester,param.get("courseid").toString(),
                param.get("staffid").toString(),param.get("classtime").toString());

        return Result.success(data,data.size());
    }


    @GetMapping("/getcoursescore")
    public List<Score>getCourseScore(@RequestParam String semester,
                                     @RequestParam String courseId,
                                     @RequestParam String courseName){
        return iSelectcourseService.getCourseScore(semester,courseId,courseName);
    }

    @GetMapping("/getstudentscore")
    public List<Score>getStudentScore(@RequestParam String semester,
                                       @RequestParam String studentId,
                                       @RequestParam String studentName){
        return iSelectcourseService.getStudentScore(semester, studentId, studentName);
    }

    @GetMapping("/getAllScore")
    public Result getAllScore(@RequestParam int pagenum,@RequestParam int pagesize){
        /*
        * 获取所有成绩信息
        * */
        List<Score> allScore = iSelectcourseService.getAllScore(pagenum,pagesize);
        return Result.success(allScore,allScore.size());
    }

    @PostMapping("/getScore")
    public Result getScore(@RequestBody QueryPageParam queryPageParam){
        /*
         * 自由搜索【学期（必须），学号，学生姓名，课号，课程名字】，得到成绩列表
         */

        int pagenum = queryPageParam.getPagenum(),pagesize = queryPageParam.getPagesize();
        HashMap param = queryPageParam.getParam();
        String semester = param.get("semester").toString();
        String studentId = param.get("studentId").toString();
        String studentName = param.get("studentName").toString();
        String courseId = param.get("courseId").toString();
        String courseName = param.get("courseName").toString();
        List<Score> data = iSelectcourseService.getScore(pagenum,pagesize,
                semester,studentId,studentName,courseId,courseName);
        return Result.success(data,data.size());

        //return iSelectcourseService.getScore(pageNum,pageSize,semester,studentId,studentName,courseId,courseName);
        /*
        * @RequestParam Integer pageNum,
        @RequestParam Integer pageSize,
        @RequestParam String semester,
        @RequestParam String studentId,
        @RequestParam String studentName,
        @RequestParam String courseId,
        @RequestParam String courseName
        * */
    }

    @PostMapping("/deleteScore")
    public Result delScore(@RequestBody QueryPageParam queryPageParam){
        /*
        * 批量，单选删除学生成绩
        * */
        HashMap param = queryPageParam.getParam();
        System.out.println(param.get("DeleteList").getClass());
        //把前端传回来的param里面的delList类型的数据转换成为List<Score>类型的
        List<LinkedHashMap<String, Object>> deleteList = (List<LinkedHashMap<String, Object>>) param.get("DeleteList");
        List<delScore> delList = new ArrayList<>();
        for(LinkedHashMap<String,Object>map:deleteList) {
            delScore scoreEle = new delScore();
            scoreEle.setSemester(map.get("semester").toString());
            scoreEle.setStudentId(map.get("studentId").toString());
            scoreEle.setCourseId(map.get("courseId").toString());
            scoreEle.setStaffId(map.get("staffId").toString());
            delList.add(scoreEle);
        }
        System.out.println(delList);
        return iSelectcourseService.AdminDelScore(delList);
    }

    @PostMapping("/editScore")
    public Result editScore(@RequestBody Score score){
        /*
        * 管理员修改成绩*/

        System.out.println(score);
        UpdateWrapper<Selectcourse> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("studentid",score.getStudentId())
                .eq("semester",score.getSemester())
                .eq("courseid",score.getCourseId())
                .eq("staffid",score.getStaffId())
                .eq("classtime",score.getClassTime())
                .set("score",score.getScore())
                .set("testScore",score.getTestScore());
        boolean success = iSelectcourseService.update(updateWrapper);
        if(success) {
            return Result.success();
        }
        return Result.fail("更新失败");
    }






}
