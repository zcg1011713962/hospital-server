package com.hs.listen;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hs.db.entity.Doctor;
import com.hs.db.entity.Schedule;
import com.hs.db.entity.ScheduleExcel;
import com.hs.db.entity.User;
import com.hs.db.mapper.DoctorMapper;
import com.hs.db.mapper.UserMapper;
import com.hs.enums.UserType;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DoctorScheduleListener extends AnalysisEventListener<ScheduleExcel> {

    private static final int BATCH_COUNT = 20;
    private final UserMapper userMapper;
    private List<ScheduleExcel> schedules = new ArrayList<>();
    private DoctorMapper doctorMapper;

    @Autowired
    public DoctorScheduleListener(DoctorMapper doctorMapper, UserMapper userMapper) {
        this.doctorMapper = doctorMapper;
        this.userMapper = userMapper;
    }

    @Override
    public void invoke(ScheduleExcel scheduleExcel, AnalysisContext context) {
        schedules.add(scheduleExcel);
        if (schedules.size() >= BATCH_COUNT) {
            saveData();
            schedules.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
    }


    public void saveData() {
        // 获取导入医生的姓名
        List<String> namas = schedules.stream().map(ScheduleExcel::getDoctorName).collect(Collectors.toList());

        QueryWrapper<Doctor> doctorWrapper = new QueryWrapper<>();
        // 构建动态条件
        if (CollectionUtils.isNotEmpty(namas)) {
            doctorWrapper.in("real_name", namas);
        }
        // 查询所有医生对应的user对象
        List<Doctor> doctorList = doctorMapper.selectList(doctorWrapper);

        // 提取用户列表并批量插入
        List<Schedule> list = schedules.stream().map(s -> {
            Schedule schedule = new Schedule();
            for (Doctor item : doctorList) {
                if (item != null && item.getRealName().equals(s.getDoctorName())) {
                    // 把医生ID关联进排班表
                    schedule.setDoctorId(item.getDoctorId());
                }
            }
            schedule.setWorkDate(s.getWorkDate());
            schedule.setStartTime(s.getStartTime());
            schedule.setEndTime(s.getEndTime());
            schedule.setMaxAppointments(s.getMaxAppointments());
            return schedule;
        }).collect(Collectors.toList());
        // 医生排班表
        doctorMapper.insertBatchSchedule(list);
    }
}

