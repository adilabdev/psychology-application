package com.adilabdullayev.psychology.mapper;

import com.adilabdullayev.psychology.dto.Request.CounselorRequest;
import com.adilabdullayev.psychology.dto.Response.CounselorResponse;
import com.adilabdullayev.psychology.model.counselor.Counselor;
import com.adilabdullayev.psychology.model.enums.CounselorSpecialization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CounselorMapper {

    @Mapping(target = "specialization", expression = "java(mapSpecialization(request.getSpecialization()))")
    Counselor toEntity(CounselorRequest request);

    CounselorResponse toResponse(Counselor entity);

    List<CounselorResponse> toResponseList(List<Counselor> counselors);

    default CounselorSpecialization mapSpecialization(String value) {
        return CounselorSpecialization.valueOf(value.toUpperCase());
    }
}

