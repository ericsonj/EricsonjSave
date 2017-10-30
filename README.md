# EricsonjSave

ericSon
   DAO
   oVer
 hibErnate

Library DAO over hibernate that implement facade pattern.

## Getting Started

Netbeans Project, clone this repo.

## Example

### Entity
```
@Entity
@Table(name = "localizations")
public class Localization implements Serializable {
    
    @Id
    private long town_code;
    private long department_code;
    private String department_name;
    private String town_name;

    public Localization() {
    }

    public Localization(long town_code, long department_code, String department_name, String town_name) {
        this.town_code = town_code;
        this.department_code = department_code;
        this.department_name = department_name;
        this.town_name = town_name;
    }

    public long getTown_code() {
        return town_code;
    }

    public void setTown_code(long town_code) {
        this.town_code = town_code;
    }

    public long getDepartment_code() {
        return department_code;
    }

    public void setDepartment_code(long department_code) {
        this.department_code = department_code;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getTown_name() {
        return town_name;
    }

    public void setTown_name(String town_name) {
        this.town_name = town_name;
    }

    @Override
    public String toString() {
        return "Localization{" + "town_code=" + town_code + ", department_code=" + department_code + ", department_name=" + department_name + ", town_name=" + town_name + '}';
    }
    
}
```
### DAO Facede

```
public class LocalizationDaoFacade extends ElementLogicFacade<Localization, Long> {

    public LocalizationDaoFacade() {
    }
    
    public LocalizationDaoFacade(SaveTransaction sf) {
        super(sf);
    }
    
    public void truncateTable() {
        super.executeQuery("TRUNCATE TABLE " + getTableName());
    }

    /**
     *
     * @param unique1
     * @param unique2
     */
    public void findByUniqueKeys(String unique1, String unique2) {
        super.findByCriteria(new CriteriaQuery() {
            @Override
            public void addCriterion(Criteria cr) {
                cr.add(Restrictions.eq("town_code", unique1));
                cr.add(Restrictions.eq("department_code", unique2));
            }
        });
    }

}
```
### Insert Element
```
LocalizationDaoFacade facade = new LocalizationDaoFacade();

facade.create(new Localization(502154,50,"DEP_NAME","TOWN_NAME"));
```
## Facade Methods
```
PK create(E Entity);

void edit(E Entity);

E find(PK id);

void remove(E Entity);

List<E> findAllByCriteria(CriteriaQuery crQuery);

E findByCriteria(CriteriaQuery crQuery);

void executeQuery(String query);

void insertBatch(int batchSize, RunnableBatch<E> runnableBatch);

List<E> findByQuery(String query, Object... params)

E findByProperty(String property, Object obj)

List<E> findAllByProperty(String property, Object obj)

List<E> findAll()

void insertBatch(int batchSize, LinkedList<E> list)

```
## Authors

* **Ericson Joseph** - *Initial work* - [EricsonjSave](https://github.com/ericsonj/EricsonjSave)




