package com.hs.listen;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.hs.db.entity.DoctorExcel;
import com.hs.db.entity.User;
import com.hs.db.mapper.DoctorMapper;
import com.hs.db.mapper.UserMapper;
import com.hs.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DoctorDataListener extends AnalysisEventListener<DoctorExcel> {

    private static final int BATCH_COUNT = 20;
    private List<DoctorExcel> doctors = new ArrayList<>();
    private DoctorMapper doctorMapper;
    private UserMapper userMapper;

    @Autowired
    public DoctorDataListener(DoctorMapper doctorMapper, UserMapper userMapper) {
        this.doctorMapper = doctorMapper;
        this.userMapper = userMapper;
    }

    @Override
    public void invoke(DoctorExcel doctor, AnalysisContext context) {
        doctors.add(doctor);
        if (doctors.size() >= BATCH_COUNT) {
            saveData();
            doctors.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
    }

    public void saveData() {
        // 提取用户列表并批量插入
        List<User> users = doctors.stream().map(doctor -> {
            String userName = doctor.getRealName() + "(" + UserType.DOCTOR.getDesc() + ")";
            User user = new User();
            user.setUserName(userName);
            user.setUserType(UserType.DOCTOR.getType());
            user.setRealName(doctor.getRealName());
            user.setPhoneNum(doctor.getPhoneNum());
            user.setEmail(doctor.getEmail());
            return user;
        }).collect(Collectors.toList());
        userMapper.insertBatchUsers(users);

        // 查询批量插入的用户数据并获取自增 ID
        List<Long> userIds = userMapper.selectUserIds(users);
        //  将生成的用户ID分配给医生
        for (int i = 0; i < doctors.size(); i++) {
            doctors.get(i).setUserId(userIds.get(i)); // 关联用户ID
        }
        doctorMapper.insertBatchDoctors(doctors); // 批量插入数据
    }
}

