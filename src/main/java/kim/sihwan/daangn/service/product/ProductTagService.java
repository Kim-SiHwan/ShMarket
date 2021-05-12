package kim.sihwan.daangn.service.product;

import kim.sihwan.daangn.domain.product.Product;
import kim.sihwan.daangn.domain.product.ProductTag;
import kim.sihwan.daangn.domain.tag.Tag;
import kim.sihwan.daangn.repository.product.ProductTagRepository;
import kim.sihwan.daangn.repository.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductTagService {
    private final TagRepository tagRepository;
    private final ProductTagRepository productTagRepository;

    public List<ProductTag> getProductIdsByTagId(Long tagId){
        return productTagRepository.findAllByTagId(tagId);
    }

    @Transactional
    public void addProductTag(Product product, Set<String> tags){
        tags.forEach(t ->{
            ProductTag productTag = new ProductTag();
            Tag tag = saveTag(t);
            productTag.addProduct(product);
            productTag.addTag(tag);
        });
    }

    @Transactional
    public Tag saveTag(String tag){
        Tag getTag = tagRepository.findTagByTag(tag)
                .orElse(Tag.builder().tag(tag).build());
        return tagRepository.save(getTag);
    }


}
