/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vbencek.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Tino
 */
@Entity
@Table(name = "DATA_LOG_EMPLOYEES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DataLogEmployees.findAll", query = "SELECT d FROM DataLogEmployees d")})
public class DataLogEmployees implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DATA_LOG_ID", nullable = false)
    private Integer dataLogId;
    @Size(max = 255)
    @Column(name = "VIEW_NAME", length = 255)
    private String viewName;
    @Size(max = 255)
    @Column(name = "METHOD_NAME", length = 255)
    private String methodName;
    @Size(max = 1000)
    @Column(length = 1000)
    private String parametars;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTION_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionDate;
    @JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "EMPLOYEE_ID", nullable = false)
    @ManyToOne(optional = false)
    private Employee employeeId;

    public DataLogEmployees() {
    }

    public DataLogEmployees(Integer dataLogId) {
        this.dataLogId = dataLogId;
    }

    public DataLogEmployees(Integer dataLogId, Date actionDate) {
        this.dataLogId = dataLogId;
        this.actionDate = actionDate;
    }

    public Integer getDataLogId() {
        return dataLogId;
    }

    public void setDataLogId(Integer dataLogId) {
        this.dataLogId = dataLogId;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParametars() {
        return parametars;
    }

    public void setParametars(String parametars) {
        this.parametars = parametars;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dataLogId != null ? dataLogId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DataLogEmployees)) {
            return false;
        }
        DataLogEmployees other = (DataLogEmployees) object;
        if ((this.dataLogId == null && other.dataLogId != null) || (this.dataLogId != null && !this.dataLogId.equals(other.dataLogId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.vbencek.model.DataLogEmployees[ dataLogId=" + dataLogId + " ]";
    }
    
}
