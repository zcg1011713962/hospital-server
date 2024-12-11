package com.hs.entity.bo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class DoctorBO {
    // 医生ID
    private Long doctorId;
    // 关联用户ID
    private Long userId;
    // 科室
    private String department;
    // 职位
    private String position;
    // 医生简介
    private String biography;
    // 擅长领域
    private String specialization;
    // 可预约时间段
    private String availableSlots;
}
