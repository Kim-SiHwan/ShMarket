
package kim.sihwan.daangn.config;

import kim.sihwan.daangn.domain.area.Area;
import kim.sihwan.daangn.domain.member.Member;
import kim.sihwan.daangn.repository.area.AreaRepository;
import kim.sihwan.daangn.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;

@Component
@RequiredArgsConstructor
class AppRunnerConfig implements ApplicationRunner {

    private final AreaRepository areaRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Member member = Member.builder()
                .username("admin")
                .nickname("admin")
                .password(passwordEncoder.encode("admin"))
                .area("인천광역시 남동구 만수3동")
                .role("ROLE_ADMIN")
                .build();
        memberRepository.save(member);

        try {
            JSONParser jsonParser = new JSONParser();
            InputStream in5 = this.getClass().getResourceAsStream("/places.json");
            System.out.println(in5);

            Object obj = jsonParser.parse(new InputStreamReader(in5));
            JSONObject jsonObj = (JSONObject) jsonParser.parse(obj.toString());

            JSONArray array = (JSONArray) jsonObj.get("features");

            for (int i = 0; i < array.size(); i++) {
                JSONObject jj = (JSONObject) array.get(i);
                JSONObject jj2 = (JSONObject) jj.get("properties");
                String city = jj2.get("sido").toString();
                String gu = jj2.get("gu").toString();
                String dong = jj2.get("dong").toString();
                String fullAddress = city + " " + gu + " " + dong;
                String lat = jj2.get("lat").toString();
                String lng = jj2.get("lng").toString();

                Area area = Area.builder()
                        .address(fullAddress)
                        .city(city)
                        .gu(gu)
                        .dong(dong)
                        .lat(lat)
                        .lng(lng)
                        .build();

                areaRepository.save(area);

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
