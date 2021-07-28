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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Tino
 */
@Entity
@Table(name = "EMPLOYEE_ROLE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EmployeeRole.findAll", query = "SELECT e FROM EmployeeRole e")})
public class EmployeeRole implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "EMPLOYEE_ROLE_ID", nullable = false)
    private Integer employeeRoleId;
    @Size(max = 255)
    @Column(length = 255)
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeRoleId")
    private List<Employee> employeeList;

    public EmployeeRole() {
    }

    public EmployeeRole(Integer employeeRoleId) {
        this.employeeRoleId = employeeRoleId;
    }

    public Integer getEmployeeRoleId() {
        return employeeRoleId;
    }

    public void setEmployeeRoleId(Integer employeeRoleId) {
        this.employeeRoleId = employeeRoleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeRoleId != null ? employeeRoleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmployeeRole)) {
            return false;
        }
        EmployeeRole other = (EmployeeRole) object;
        if ((this.employeeRoleId == null && other.employeeRoleId != null) || (this.employeeRoleId != null && !this.employeeRoleId.equals(other.employeeRoleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.vbencek.model.EmployeeRole[ employeeRoleId=" + employeeRoleId + " ]";
    }
    
}
