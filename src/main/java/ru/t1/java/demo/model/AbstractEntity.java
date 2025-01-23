package ru.t1.java.demo.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;
import org.springframework.data.util.ProxyUtils;

import java.io.Serializable;

@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractEntity<PK extends Serializable> implements Persistable<PK> {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    private PK id;

    @Nullable
    public PK getId() {
        return this.id;
    }

    protected void setId(@Nullable PK id) {
        this.id = id;
    }

    @Transient
    public boolean isNew() {
        return this.getId() == null;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (!this.getClass().equals(ProxyUtils.getUserClass(obj))) {
            return false;
        } else {
            AbstractEntity<?> that = (AbstractEntity)obj;
            return this.getId() != null && this.getId().equals(that.getId());
        }
    }

    public int hashCode() {
        int hashCode = 17;
        hashCode += this.getId() == null ? 0 : this.getId().hashCode() * 31;
        return hashCode;
    }
}
