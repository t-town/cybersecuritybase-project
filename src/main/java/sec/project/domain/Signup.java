package sec.project.domain;

import javax.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Signup extends AbstractPersistable<Long> {

    private String name;
    private String address;
    private String password;
    private String pledge;

    public Signup() {
        super();
    }

    public Signup(String name, String address, String password, String pledge) {
        this();
        this.name = name;
        this.address = address;
        this.password = password;
        this.pledge = pledge;
    }

    public String getPassword() {
        return password;
    }

    public String getPledge() {
        return pledge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void changePledge(String newPledge) {
        this.pledge = newPledge;
    }

}
