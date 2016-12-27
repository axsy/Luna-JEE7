package com.alekseyorlov.luna.domain.listener.support;

import com.github.slugify.Slugify;

public class DefaultSlugGenerationStrategy implements SlugGenerationStrategy  {

    private Slugify slugify = new Slugify();

    @Override
    public String generate(String source) {

        return slugify.slugify(source);
    }

}
