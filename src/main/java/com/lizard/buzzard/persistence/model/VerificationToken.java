package com.lizard.buzzard.persistence.model;

import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "verification_property")
@PropertySource("classpath:lizard.config.properties")
@ConfigurationProperties(prefix = "lizard")
@EqualsAndHashCode(exclude="user")
public class VerificationToken {

//    @Transient
//    @Value("${lizard.verivication.token.expiration}")
//    private String test;
//    private Long expirationInMinutes = Long.valueOf(60 * 24);
    private Long expirationInMinutes = Long.valueOf(1);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
//    @MapsId
    @JoinColumn(nullable = false, name = "user_id", foreignKey = @ForeignKey(name = "FK_VERIFY_USER"))
    User user;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column
    private String token;

    public VerificationToken() {
        super();
    }

    public VerificationToken(final String token) {
        super();

        this.token = token;
        this.expirationDate = calculateExpiryDate(expirationInMinutes);
    }

    public VerificationToken(final String token, final User user) {
        super();

        this.token = token;
        this.user = user;
        this.expirationDate = calculateExpiryDate(expirationInMinutes);
    }

    private Date calculateExpiryDate(Long expirationInMinutes) {
        Instant now = Instant.now();
        Duration expirationPeriod = Duration.ofMinutes(expirationInMinutes);
        Instant expiredAt = now.plus(expirationPeriod);
        return Date.from(expiredAt);
    }

    public Long getExpirationInMinutes() {
        return expirationInMinutes;
    }

    public void setExpirationInMinutes(Long expirationInMinutes) {
        this.expirationInMinutes = expirationInMinutes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VerificationToken)) return false;
        VerificationToken that = (VerificationToken) o;
        return Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getExpirationDate(), that.getExpirationDate()) &&
                Objects.equals(getToken(), that.getToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getExpirationDate(), getToken());
    }

    @Override
    public String toString() {
        return "VerificationToken{" +
                "id=" + id +
//                ", user=" + user +
                ", expirationDate=" + expirationDate +
                ", token='" + token + '\'' +
                '}';
    }
}
