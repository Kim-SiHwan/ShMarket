package kim.sihwan.daangn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kim.sihwan.daangn.dto.board.BoardRequestDto;
import kim.sihwan.daangn.dto.board.BoardResponseDto;
import kim.sihwan.daangn.dto.board.BoardUpdateRequestDto;
import kim.sihwan.daangn.dto.common.Result;
import kim.sihwan.daangn.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Api(tags = "4. Board")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @ApiOperation(value = "페이지로 게시글 조회", notes = "전체 게시글 페이징 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryList", dataType = "List<String>", value = "게시글 카테고리 리스트", required = true),
            @ApiImplicitParam(name = "nickname", dataType = "String", value = "조회 대상 닉네임 ( 없다면 전체 게시글 조회 )", required = false),
            @ApiImplicitParam(name = "page", dataType = "int", value = "페이지 번호 ( 없다면 첫번째 페이지 조회 )", required = false)
    })
    @GetMapping("/list/{page}")
    public ResponseEntity<Result> getBoardsByPaging(@RequestParam(value = "list") List<String> categories,
                                                    @RequestParam(required = false) String nickname,
                                                    @PathVariable(required = false) int page) {
        return new ResponseEntity<>(boardService.paging(page, categories, nickname), HttpStatus.OK);
    }

    @ApiOperation(value = "게시글 단건 조회", notes = "게시글의 PK로 게시글 단건 조회")
    @ApiImplicitParam(name = "boardId", dataType = "Long", value = "조회 대상 게시글 PK", required = true)
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> findById(@PathVariable Long boardId) {
        return new ResponseEntity<>(boardService.findById(boardId), HttpStatus.OK);
    }

    @ApiOperation(value = "닉네임으로 게시글 조회", notes = "사용자의 닉네임으로 해당 사용자의 게시글 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickname", dataType = "String", value = "조회 대상 닉네임", required = true),
            @ApiImplicitParam(name = "page", dataType = "int", value = "페이지 번호", required = true)
    })
    @GetMapping("/my/{page}")
    public ResponseEntity<Result> findAllBoardByNickname(@RequestParam String nickname,
                                                         @PathVariable int page) {
        return new ResponseEntity<>(boardService.findAllBoardByNickname(page, nickname), HttpStatus.OK);
    }

    @ApiOperation(value = "이미지 조회", notes = "URL로 이미지 조회")
    @ApiImplicitParam(name = "fileName", dataType = "String", value = "이미지의 URL", required = true)
    @GetMapping(value = "/download", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileName") String fileName) {
        Resource resource = new FileSystemResource("C:\\Users\\김시환\\Desktop\\Git\\DaangnMarket-Clone\\src\\main\\resources\\images\\" + fileName);
        return new ResponseEntity(resource, HttpStatus.OK);
    }

    @ApiOperation(value = "게시글 생성", notes = "게시글 생성")
    @PostMapping
    public void addBoard(@Valid @ModelAttribute BoardRequestDto boardRequestDto) {
        boardService.addBoard(boardRequestDto);
    }

    @ApiOperation(value = "게시글 삭제", notes = "PK로 게시글 삭제")
    @ApiImplicitParam(name = "", dataType = "", value = "", required = true)
    @DeleteMapping("/{boardId}")
    public void deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
    }

    @ApiOperation(value = "게시글 수정", notes = "게시글 수정")
    @PutMapping
    public ResponseEntity<BoardResponseDto> updateBoard(@Valid @ModelAttribute BoardUpdateRequestDto updateRequestDto) {
        return new ResponseEntity<>(boardService.updateBoard(updateRequestDto), HttpStatus.OK);
    }
}
