package com.kroum.kroum.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "A_SaveSearchHistoryDto", description = "검색 기록 저장용 DTO")
public class SaveSearchHistoryRequestDto {

    @Schema(description = "검색 문장")
    private String query;

}
