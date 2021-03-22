package top.daxianwill.greendaodemo;

/**
 * 创建数据库实体类
 *
 *   @Entity 表示这个实体类一会会在数据库中生成对应的表，

     @Id 表示该字段是id，注意该字段的数据类型为包装类型Long

     @Property 则表示该属性将作为表的一个字段，其中nameInDb看名字就知道这个属性在数据库中对应的数据名称。

     @Transient 该注解表示这个属性将不会作为数据表中的一个字段。

     @NotNull 表示该字段不可以为空

     @Unique 表示该字段唯一
 */
//@Entity
public class User {
//    @Id
//    private Long id;
//
//    @Property(nameInDb = "NAME")
//    private String name;
//
//    @Transient
//    private int tempUsageCount; // not persisted
//
//    @Generated(hash = 873297011)
//    public User(Long id, String name) {
//        this.id = id;
//        this.name = name;
//    }
//    @Generated(hash = 586692638)
//    public User() {
//    }
//    public Long getId() {
//        return this.id;
//    }
//    public void setId(Long id) {
//        this.id = id;
//    }
//    public String getName() {
//        return this.name;
//    }
//    public void setName(String name) {
//        this.name = name;
//    }
}
