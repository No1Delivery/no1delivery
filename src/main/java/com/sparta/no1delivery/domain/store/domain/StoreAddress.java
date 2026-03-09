package com.sparta.no1delivery.domain.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.no1delivery.global.domain.service.AddressToCoords;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.util.StringUtils;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreAddress {

    @Column(name = "address")
    private String address;

    @Column(name = "detail_address")
    private String detailAddress;

    // PostGIS의 Geometry 타입으로 변경
    // SRID 4326은 WGS84(위도, 경도) 좌표계를 의미합니다.
    @JsonIgnore // 직렬화 오류 방지
    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point point;

    @Column(name = "latitude")
    private double latitude; // 위도

    @Column(name = "longitude")
    private double longitude; // 경도

    protected StoreAddress(String address, String detailAddress, AddressToCoords addressToCoords) {
        this.address = address;
        this.detailAddress = detailAddress;

        if (!StringUtils.hasText(address) || !StringUtils.hasText(detailAddress)) return;

        double[] coords = addressToCoords.convert(address);
        latitude = coords[0];
        longitude = coords[1];

        // JTS GeometryFactory를 이용해 Point 객체 생성
        GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
        point = factory.createPoint(new Coordinate(longitude, latitude));
    }
}
