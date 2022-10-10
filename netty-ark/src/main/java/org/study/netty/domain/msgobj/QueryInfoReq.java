package org.study.netty.domain.msgobj;

public class QueryInfoReq {
    private Integer stateType;//类型；Feedback=>{1\2}

    public QueryInfoReq(Integer stateType) {
        this.stateType = stateType;
    }

    public Integer getStateType() {
        return stateType;
    }

    public void setStateType(Integer stateType) {
        this.stateType = stateType;
    }
}
