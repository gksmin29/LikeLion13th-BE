package com.project.likelion13thbe.domain.member.controller;

import com.project.likelion13thbe.domain.member.dto.request.MemberReqDTO;
import com.project.likelion13thbe.domain.member.dto.response.MemberResDTO;
import com.project.likelion13thbe.domain.member.entity.Member;
import com.project.likelion13thbe.domain.member.service.command.MemberCommandService;
import com.project.likelion13thbe.domain.member.service.query.MemberQueryService;
import com.project.likelion13thbe.domain.review.dto.response.ReviewResDTO;
import com.project.likelion13thbe.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "멤버관련", description = "멤버 관련 API")
public class MemberController {
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

    @GetMapping
    public ResponseEntity<MemberResDTO.MemberPreviewResDTO> getMember(
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        return ResponseEntity.ok(memberQueryService.getMember());
    }

    public ResponseEntity<MemberResDTO.MemberOffsetResDTO> getMemberOffset(
            @RequestParam Integer offset,
            @RequestParam Integer size
    ) {
        return ResponseEntity.ok(memberQueryService.getMemberOffset(offset, size));
    }

    //카카오 로그인
    @Operation(summary = "카카오 로그인 API", description = "카카오 로그인 관련 API")
    @PostMapping("/auth/kakao")
    public CustomResponse<String> kakaoLogin() { return null; } //request는 구현하지 않았음.

    //일반 로그인
    @Operation(summary = "일반 로그인 API", description = "일반 로그인 API입니다.")
    @PostMapping("/auth/login")
    public CustomResponse<String> login() { return null; } //request는 구현하지 않았음.

    //비밀번호 수정
    @Operation(summary = "비밀번호 수정 API", description = "비밀번호 수정 API입니다.")
    @PatchMapping("/members")
    @Validated
    public CustomResponse<String> resetPassword(
            @RequestBody @NotNull MemberReqDTO.PasswordResetDTO passwordResetDTO

    ){
        memberCommandService.updatePassword(passwordResetDTO);
        return CustomResponse.onSuccess("비밀번호 변경 성공");
    }
    //회원가입
    @Operation(summary = "회원가입 API", description = "회원가입 API입니다.")
    @PostMapping("/members")
    public CustomResponse<String> createMember
    (@RequestBody MemberReqDTO.MemberCreateReqDTO memberCreateReqDTO) {

        memberCommandService.createMember(memberCreateReqDTO);

        return CustomResponse.onSuccess("회원가입 성공");
    }

    //회원 탈퇴 (JWT 인증 필요)
    public CustomResponse<String> deleteMember(
            @RequestBody MemberReqDTO.MemberDeleteDTO memberDeleteDTO
    ) {
        memberCommandService.deleteMember(memberDeleteDTO);
        return CustomResponse.onSuccess("회원 탈퇴 성공");
    }
}
