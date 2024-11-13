package com.locat.api.global.mail;

import static j2html.TagCreator.*;

/**
 * 메일 템플릿을 정의하는 클래스 <br>
 * <li>{@code j2html}을 사용하여 HTML 메일 템플릿을 생성 & 관리
 */
public final class MailTemplate {

  private MailTemplate() {
    // Utility class
  }

  /** HTML 메일 템플릿의 공통 스타일입니다. */
  private static final String COMMON_TEMPLATE_STYLE =
      "font-family: 'Noto Sans KR', sans-serif; width: 100%; height: 100%; text-align: center; color: #333;";

  public static final String MAIL_VERIFY_TITLE = "[LOCAT] 이메일 본인 인증 코드";

  public static String createMailVerifyMessage(final String verificationCode) {
    return html(
            head(title("이메일 본인 인증 코드를 확인해주세요.")),
            body(
                div(
                        h1("이메일 본인 인증 메일입니다."),
                        p("서비스를 안전하게 즐기기 위해 아래의 인증 코드를 입력해주세요."),
                        h1(verificationCode)
                            .withStyle(
                                "font-size: 30px; font-weight: bold; margin-bottom: 20px; margin-top: 20px;"),
                        p("- 인증 코드는 5분간 유효하며, 유효 시간이 지난 경우 다시 요청해주세요."),
                        p("- 타인이 이메일을 잘못 입력한 경우, 본 메일이 발송될 수 있습니다."))
                    .withStyle(COMMON_TEMPLATE_STYLE)))
        .render();
  }
}
