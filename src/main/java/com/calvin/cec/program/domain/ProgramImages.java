package com.calvin.cec.program.domain;

public record ProgramImages(
        String thumbnail, // DB: image
        String banner, // DB: banner_image
        String detail // DB: detail_image
) {

    public static ProgramImages empty() {
        return new ProgramImages(null, null, null);
    }

}
