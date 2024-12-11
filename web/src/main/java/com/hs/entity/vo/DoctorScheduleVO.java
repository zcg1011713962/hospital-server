package com.hs.entity.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorScheduleVO {
    private Long doctorID;
    private String doctorName; // 医生姓名
    private String department; // 科室
    private String position; // 职位
    private String startTime; // 上班时间
    private String endTime; // 下班时间
    private String maxAppointments; // 可预约人数上限
    private String bookedAppointments; // 已预约人数
    private boolean isExistNum; // 是否有号

}
