package ru.iteco.fmh.dto.note;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.iteco.fmh.dto.patient.PatientDto;
import ru.iteco.fmh.dto.user.UserDto;
import ru.iteco.fmh.model.StatusE;

import java.time.LocalDateTime;

@ApiModel(description = "записка")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class NoteDto {
    @ApiModelProperty("идентификатор записки")
    private Integer id;
    @ApiModelProperty("идентификатор пациента")
    private PatientDto patient;
    @ApiModelProperty("описание записки")
    private String description;
    @ApiModelProperty("идентификатор исполнителя")
    private UserDto executor;
    @ApiModelProperty("плановое время исполнения")
    private LocalDateTime planExecuteTime;
    @ApiModelProperty("фактическое время исполнения")
    private LocalDateTime factExecuteTime;
    @ApiModelProperty("статус записки")
    private StatusE status;
    @ApiModelProperty("комментарий к записке")
    private String comment;
}
