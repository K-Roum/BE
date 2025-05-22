package com.kroum.kroum.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContentIdDto {

    private Long contentId;

    public ContentIdDto(String contentId) {
        this.contentId = Long.parseLong(contentId);
    }
}
