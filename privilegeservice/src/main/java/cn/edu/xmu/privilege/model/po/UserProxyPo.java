package cn.edu.xmu.privilege.model.po;

import java.time.LocalDateTime;

public class UserProxyPo {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_user_proxy.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_user_proxy.user_a_id
     *
     * @mbg.generated
     */
    private Long userAId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_user_proxy.user_b_id
     *
     * @mbg.generated
     */
    private Long userBId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_user_proxy.begin_date
     *
     * @mbg.generated
     */
    private LocalDateTime beginDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_user_proxy.end_date
     *
     * @mbg.generated
     */
    private LocalDateTime endDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_user_proxy.gmt_create
     *
     * @mbg.generated
     */
    private LocalDateTime gmtCreate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column auth_user_proxy.signature
     *
     * @mbg.generated
     */
    private String signature;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_user_proxy.id
     *
     * @return the value of auth_user_proxy.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_user_proxy.id
     *
     * @param id the value for auth_user_proxy.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_user_proxy.user_a_id
     *
     * @return the value of auth_user_proxy.user_a_id
     *
     * @mbg.generated
     */
    public Long getUserAId() {
        return userAId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_user_proxy.user_a_id
     *
     * @param userAId the value for auth_user_proxy.user_a_id
     *
     * @mbg.generated
     */
    public void setUserAId(Long userAId) {
        this.userAId = userAId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_user_proxy.user_b_id
     *
     * @return the value of auth_user_proxy.user_b_id
     *
     * @mbg.generated
     */
    public Long getUserBId() {
        return userBId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_user_proxy.user_b_id
     *
     * @param userBId the value for auth_user_proxy.user_b_id
     *
     * @mbg.generated
     */
    public void setUserBId(Long userBId) {
        this.userBId = userBId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_user_proxy.begin_date
     *
     * @return the value of auth_user_proxy.begin_date
     *
     * @mbg.generated
     */
    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_user_proxy.begin_date
     *
     * @param beginDate the value for auth_user_proxy.begin_date
     *
     * @mbg.generated
     */
    public void setBeginDate(LocalDateTime beginDate) {
        this.beginDate = beginDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_user_proxy.end_date
     *
     * @return the value of auth_user_proxy.end_date
     *
     * @mbg.generated
     */
    public LocalDateTime getEndDate() {
        return endDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_user_proxy.end_date
     *
     * @param endDate the value for auth_user_proxy.end_date
     *
     * @mbg.generated
     */
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_user_proxy.gmt_create
     *
     * @return the value of auth_user_proxy.gmt_create
     *
     * @mbg.generated
     */
    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_user_proxy.gmt_create
     *
     * @param gmtCreate the value for auth_user_proxy.gmt_create
     *
     * @mbg.generated
     */
    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column auth_user_proxy.signature
     *
     * @return the value of auth_user_proxy.signature
     *
     * @mbg.generated
     */
    public String getSignature() {
        return signature;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column auth_user_proxy.signature
     *
     * @param signature the value for auth_user_proxy.signature
     *
     * @mbg.generated
     */
    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
    }
}