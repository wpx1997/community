package life.wpx1997.community.model;

public class CommunityLike {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column community_like.id
     *
     * @mbg.generated Mon Jul 27 23:32:45 CST 2020
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column community_like.parent_id
     *
     * @mbg.generated Mon Jul 27 23:32:45 CST 2020
     */
    private Long parentId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column community_like.user_id
     *
     * @mbg.generated Mon Jul 27 23:32:45 CST 2020
     */
    private Long userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column community_like.type
     *
     * @mbg.generated Mon Jul 27 23:32:45 CST 2020
     */
    private Byte type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column community_like.is_delete
     *
     * @mbg.generated Mon Jul 27 23:32:45 CST 2020
     */
    private Byte isDelete;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column community_like.gmt_create
     *
     * @mbg.generated Mon Jul 27 23:32:45 CST 2020
     */
    private Long gmtCreate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column community_like.gmt_modified
     *
     * @mbg.generated Mon Jul 27 23:32:45 CST 2020
     */
    private Long gmtModified;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column community_like.id
     *
     * @return the value of community_like.id
     *
     * @mbg.generated Mon Jul 27 23:32:45 CST 2020
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column community_like.id
     *
     * @param id the value for community_like.id
     *
     * @mbg.generated Mon Jul 27 23:32:45 CST 2020
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column community_like.parent_id
     *
     * @return the value of community_like.parent_id
     *
     * @mbg.generated Mon Jul 27 23:32:45 CST 2020
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column community_like.parent_id
     *
     * @param parentId the value for community_like.parent_id
     *
     * @mbg.generated Mon Jul 27 23:32:45 CST 2020
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column community_like.user_id
     *
     * @return the value of community_like.user_id
     *
     * @mbg.generated Mon Jul 27 23:32:45 CST 2020
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column community_like.user_id
     *
     * @param userId the value for community_like.user_id
     *
     * @mbg.generated Mon Jul 27 23:32:45 CST 2020
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column community_like.type
     *
     * @return the value of community_like.type
     *
     * @mbg.generated Mon Jul 27 23:32:45 CST 2020
     */
    public Byte getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column community_like.type
     *
     * @param type the value for community_like.type
     *
     * @mbg.generated Mon Jul 27 23:32:45 CST 2020
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column community_like.is_delete
     *
     * @return the value of community_like.is_delete
     *
     * @mbg.generated Mon Jul 27 23:32:45 CST 2020
     */
    public Byte getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column community_like.is_delete
     *
     * @param isDelete the value for community_like.is_delete
     *
     * @mbg.generated Mon Jul 27 23:32:45 CST 2020
     */
    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column community_like.gmt_create
     *
     * @return the value of community_like.gmt_create
     *
     * @mbg.generated Mon Jul 27 23:32:45 CST 2020
     */
    public Long getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column community_like.gmt_create
     *
     * @param gmtCreate the value for community_like.gmt_create
     *
     * @mbg.generated Mon Jul 27 23:32:45 CST 2020
     */
    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column community_like.gmt_modified
     *
     * @return the value of community_like.gmt_modified
     *
     * @mbg.generated Mon Jul 27 23:32:45 CST 2020
     */
    public Long getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column community_like.gmt_modified
     *
     * @param gmtModified the value for community_like.gmt_modified
     *
     * @mbg.generated Mon Jul 27 23:32:45 CST 2020
     */
    public void setGmtModified(Long gmtModified) {
        this.gmtModified = gmtModified;
    }
}