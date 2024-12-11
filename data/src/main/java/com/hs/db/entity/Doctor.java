package com.hs.db.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Doctor{
    // 用户ID
    private Long userId;
    // 医生ID
    private Long doctorId;
    // 医生姓名
    private String realName;
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
