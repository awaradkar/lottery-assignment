package com.poppulo.lottery.repository;

import com.poppulo.lottery.model.Lottery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotteryRepository extends JpaRepository<Lottery, Long> {


}
