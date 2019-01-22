package challenge;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "scripts")
public class Quote implements Serializable {

    private static final long serialVersionUID = -3321445520401823006L;

    @Id
    private Integer id;
    private String actor;
    private String detail;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
        this.id = id;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
        this.actor = actor;
	}

	public String getQuote() {
		return detail;
	}

	public void setQuote(String quote) {
        this.detail = quote;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quote)) return false;
        Quote quote = (Quote) o;
        return Objects.equals(id, quote.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
