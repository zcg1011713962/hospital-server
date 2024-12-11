package com.hs.db.entity;

import lombok.Data;

@Data
public class DoctorSchedule {
    private Long doctorID; // 医生ID
    private String doctorName; // 医生姓名
    private String department; // 科室
    private String position; // 职位
    private String startTime; // 上班时间
    private String endTime; // 下班时间
    private String maxAppointments; // 可预约人数上限
    private String bookedAppointments; // 已预约人数
}
