import java.time.LocalDate;

public class Loan {
    private Integer id;
    private String loadType;
    private LocalDate date;

    public Loan(Integer id, String loadType, LocalDate date) {
        this.id = id;
        this.loadType = loadType;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getLoadType() {
        return loadType;
    }
    public void setLoadType(String loadType) {
        this.loadType = loadType;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
 
    @Override
    public String toString(){
        return "Loan [id=" + id + ", loadType=" + loadType + ", date=" + date + "]";
    }
    
}
    