package com.back.settlement.adapter.in;

import com.back.common.code.SuccessCode;
import com.back.common.response.CommonResponse;
import com.back.settlement.app.dto.response.SettlementItemResponse;
import com.back.settlement.app.dto.response.SettlementResponse;
import com.back.settlement.app.facade.SettlementFacade;
import com.back.settlement.batch.SettlementJobLauncher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/settlements")
@RequiredArgsConstructor
public class SettlementControllerV1 implements SettlementApiV1 {
    private final SettlementFacade settlementFacade;
    private final SettlementJobLauncher settlementJobLauncher;

    @GetMapping
    public CommonResponse<List<SettlementResponse>> getSettlements() {
        // TODO: 권한 체크 필요 (인증된 사용자에서 sellerId 추출)
        Long sellerId = 1L;
        List<SettlementResponse> response = settlementFacade.getSettlements(sellerId);
        return CommonResponse.success(SuccessCode.OK, response);
    }

    @GetMapping("/{settlementItemId}")
    public CommonResponse<SettlementItemResponse> getSettlementItem(@PathVariable("settlementItemId") Long settlementItemId) {
        // TODO: 권한 체크 필요 (인증된 사용자에서 sellerId 추출)
        Long sellerId = 1L;
        SettlementItemResponse response = settlementFacade.getSettlementItem(settlementItemId, sellerId);
        return CommonResponse.success(SuccessCode.OK, response);
    }

    @Operation(summary = "정산 배치 수동 실행", description = "특정 월의 정산 배치를 수동으로 실행합니다. 테스트 목적으로 사용합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배치 실행 완료"),
            @ApiResponse(responseCode = "500", description = "배치 실행 실패")
    })
    @PostMapping("/batch/run")
    public CommonResponse<BatchExecutionResponse> runSettlementBatch(
            @Parameter(description = "정산 대상 월 (yyyy-MM 형식)", example = "2024-01")
            @RequestParam("targetMonth") @DateTimeFormat(pattern = "yyyy-MM") YearMonth targetMonth
    ) {
        JobExecution execution = settlementJobLauncher.run(targetMonth);
        BatchExecutionResponse response = new BatchExecutionResponse(
                execution.getId(),
                execution.getStatus().toString(),
                targetMonth.toString(),
                execution.getStartTime(),
                execution.getEndTime()
        );
        return CommonResponse.success(SuccessCode.OK, response);
    }

    public record BatchExecutionResponse(
            Long jobId,
            String status,
            String targetMonth,
            java.time.LocalDateTime startTime,
            java.time.LocalDateTime endTime
    ) {}
}
