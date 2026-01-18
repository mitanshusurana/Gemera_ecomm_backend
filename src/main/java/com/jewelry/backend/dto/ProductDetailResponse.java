package com.jewelry.backend.dto;

import com.jewelry.backend.entity.embeddable.PriceBreakup;
import com.jewelry.backend.entity.embeddable.ProductImage;
import com.jewelry.backend.entity.embeddable.ProductSpecification;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductDetailResponse extends ProductResponse {
    private List<ProductImage> images;
    private ProductSpecification specifications;
    private List<UUID> relatedProducts;
    private PriceBreakup priceBreakup;
}
