package com.scj.common.enums.handler;

import com.scj.common.enums.JobTypeEnum;
import com.scj.common.enums.WebPageEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by shengchaojie on 2017/6/21.
 */
public class JobTypeEnumHandler extends BaseTypeHandler<JobTypeEnum> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, JobTypeEnum jobTypeEnum, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i,jobTypeEnum.ordinal());
    }

    @Override
    public JobTypeEnum getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return  JobTypeEnum.getEnumByCode(resultSet.getInt(s));
    }

    @Override
    public JobTypeEnum getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return JobTypeEnum.getEnumByCode(resultSet.getInt(i));
    }

    @Override
    public JobTypeEnum getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return JobTypeEnum.getEnumByCode(callableStatement.getInt(i));
    }
}
