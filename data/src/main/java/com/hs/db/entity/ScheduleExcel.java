package com.hs.db.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ScheduleExcel {
    @ExcelProperty("医生姓名")
    private String doctorName; // 医生姓名
    @ExcelProperty("所属科室")
    private String department; // 科室
    @ExcelProperty("工作日期")
    private String workDate; // 工作日期
    @ExcelProperty("上班时间")
    private String startTime; // 上班时间
    @ExcelProperty("下班时间")
    private String endTime; // 下班时间
    @ExcelProperty("最大预约人数")
    private Integer maxAppointments; // 最大预约人数
}
