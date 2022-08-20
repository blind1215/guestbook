package org.zerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository myDream;

    @Test
    public void insertDummies(){
        IntStream.rangeClosed(1,300).forEach(i->{
            Guestbook guestbook = Guestbook.builder()
                    .title("Title...." + i)
                    .content("Content...." + i)
                    .writer("user"+(i%10))
                    .build();
            System.out.println(myDream.save(guestbook));
        });
    }

    @Test
    public void updateTest(){
        Optional<Guestbook> result = myDream.findById(300L);

        if(result.isPresent()){
            Guestbook guestbook = result.get();

            guestbook.changeTitle("Change Title....");
            guestbook.changeContent("Change Content....");

            myDream.save(guestbook);
        }
    }

    @Test
    public void testQuery1(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook; //1

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder(); // 2
        BooleanExpression expression = qGuestbook.title.contains(keyword); // 3
        builder.and(expression);//4

        Page<Guestbook> result = myDream.findAll(builder,pageable); // 5

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });

    }

    @Test
    public void testQuery2(){
        Pageable pageable = PageRequest.of(0,10,Sort.by("gno").descending());

        QGuestbook lotto = QGuestbook.guestbook;

        String keyword = "lotto";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exTitle = lotto.title.contains(keyword);
        BooleanExpression exContent = lotto.content.contains(keyword);

        BooleanExpression exAll = exTitle.or(exContent);
        builder.and(exAll);
        builder.and(lotto.gno.gt(0L));

        Page<Guestbook> result = myDream.findAll(builder, pageable);

        result.stream().forEach(pleaseLotto -> {
            System.out.println(pleaseLotto);
        });
        
        if(result.isEmpty()){
            System.out.println("너는 이번주에도 로또에 맞지 않았다. 열심히 일하거라");
        }
        
        
    }
}
