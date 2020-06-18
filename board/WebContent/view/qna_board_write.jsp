<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@include file="../include/header.jsp"%>
<style>
	.text-info{
	color: red;
	font-size: 0.8rem;
	}
	span.error{
		color: red;
		font-size: 0.8rem;
	}
	
</style>
<!-- Main content -->
<section class="content">
  <div class="box box-primary">
    <div class="box-header">
      <h3 class="box-title">Board Write</h3>
    </div>
    <div style="height:20px"></div>
    <form action="../write.do" method="post" enctype="multipart/form-data" role="form" id="writeForm">
      <div class="box-body">
        <div class="form-group row">
          <label for="name" class="col-sm-2 col-form-label">작성자</label>
          <div class="col-sm-10">
            <input
              type="text"
              name="name"
              id="name"
              size="10"
              class="form-control"
              maxlength="10"
            />
          </div>
        </div>
        <div class="form-group row">
          <label for="title" class="col-sm-2 col-form-label">제목</label>
          <div class="col-sm-10">
            <input
              type="text"
              name="title"
              id="title"
              size="50"
              class="form-control"
              maxlength="100"
            />
          </div>
          
        </div>
        <div class="form-group row">
          <label for="content" class="col-sm-2 col-form-label">내용</label>
          <div class="col-sm-10">
            <textarea
              name="content"
              id="content"
              cols="60"
              class="form-control"
              rows="15"
            ></textarea>
          </div>         
        </div>
        <div class="form-group row">
          <label for="password" class="col-sm-2 col-form-label">비밀번호</label>
          <div class="col-sm-10">
            <input
              type="password"
              name="password"
              id="password"
              class="form-control"
              size="10"
              maxlength="10"
            />
          </div>          
        </div>
        <div class="form-group row">
          <label for="file" class="col-sm-2 col-form-label">파일첨부</label>
          <div class="col-sm-10">
            <input type="file" name="attach" id="file" />
            <small class="text-muted">(파일크기 : 2MB)</small>
            <small id="file" class="text=info"></small>
          </div>
        </div>
        <div style="height:20px"></div>
        <div class="form-group text-center">
          <button type="submit" class="btn btn-primary">등록</button>
          <button type="reset" class="btn btn-danger">다시작성</button>
          <button type="button" class="btn btn-warning" id="list" onclick="locaction.href='../list.do'">목록보기
          </button>
        </div>
        <div style="height:20px"></div>
      </div>
    </form>
  </div>
  <!-- /.box -->
</section>
<%-- 사용자 validate 코드 삽입하기 --%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<!-- jquery-validation plugin -->
<script src="https://cdn.jsdelivr.net/npm/jquery-validation@1.19.2/dist/jquery.validate.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/jquery-validation@1.19.2/dist/additional-methods.min.js"></script>
<!-- 사용자 작성 유효성 검증 -->
<script src="../js/write.js"></script>
<%@include file="../include/footer.jsp"%>
