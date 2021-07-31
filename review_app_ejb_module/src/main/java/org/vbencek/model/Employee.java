/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Tino
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e")})
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "EMPLOYEE_ID", nullable = false)
    private Integer employeeId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(nullable = false, length = 25)
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String password;
    @Size(max = 255)
    @Column(length = 255)
    private String firstname;
    @Size(max = 255)
    @Column(length = 255)
    private String lastname;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String email;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Boolean isblocked;
    @JoinColumn(name = "EMPLOYEE_ROLE_ID", referencedColumnName = "EMPLOYEE_ROLE_ID", nullable = false)
    @ManyToOne(optional = false)
    private EmployeeRole employeeRoleId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeId")
    private List<DataLogEmployees> dataLogEmployeesList;

    public Employee() {
    }

    public Employee(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Employee(Integer employeeId, String username, String password, String email, Boolean isblocked) {
        this.employeeId = employeeId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.isblocked = isblocked;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsblocked() {
        return isblocked;
    }

    public void setIsblocked(Boolean isblocked) {
        this.isblocked = isblocked;
    }

    public EmployeeRole getEmployeeRoleId() {
        return employeeRoleId;
    }

    public void setEmployeeRoleId(EmployeeRole employeeRoleId) {
        this.employeeRoleId = employeeRoleId;
    }

    @XmlTransient
    public List<DataLogEmployees> getDataLogEmployeesList() {
        return dataLogEmployeesList;
    }

    public void setDataLogEmployeesList(List<DataLogEmployees> dataLogEmployeesList) {
        this.dataLogEmployeesList = dataLogEmployeesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeId != null ? employeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.employeeId == null && other.employeeId != null) || (this.employeeId != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.vbencek.model.Employee[ employeeId=" + employeeId + " ]";
    }
    
}
