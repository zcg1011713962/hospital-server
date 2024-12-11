package com.hs.db.entity;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Schedule {
    private Long doctorId; // 医生ID
    private String doctorName; // 医生姓名
    private String department; // 科室
    private String workDate; // 工作日期
    private String startTime; // 上班时间
    private String endTime; // 下班时间
    private Integer maxAppointments; // 最大预约人数
}
