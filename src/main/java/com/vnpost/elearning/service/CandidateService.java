package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.CandidateConverter;
import com.vnpost.elearning.dto.competition.CandidateDTO;
import com.vnpost.elearning.security.MyUser;
import eln.common.core.entities.competition.Candidate;
import eln.common.core.repository.CandidateRepository;
import eln.common.core.repository.ResultRepository;
import eln.common.core.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidateService {

  @Autowired private CandidateConverter candidateConverter;

  @Autowired private CandidateRepository candidateRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private RoundTestService roundTestService;
  @Autowired private ResultRepository resultRepository;

  @Autowired EntityManager entityManager;

  public Integer countByidUserIdRoundIdCount(Long idUser, Long idRound, String countTest) {
    return candidateRepository.countByidUserIdRoundIdCount(idUser, idRound, countTest);
  }

  public Integer countByidUserIdRound(Long idUser, Long idRound) {
    return candidateRepository.countByidUserIdRound(idUser, idRound);
  }

  public Integer findByIdUser(String id_user, String id_round_test) {
    return candidateRepository.findByIdUser(id_user, id_round_test);
  }

  public Integer findByIdUserByIgnoreCount0(String id_user, String id_round_test) {
    return candidateRepository.findByIdUserByIgnoreCount0(id_user, id_round_test);
  }

  public Integer checkLockCandidate(Long id_user, Long id_round_test) {
    return candidateRepository.checkLockCandidate(id_user, id_round_test);
  }

  public Integer findByIdUserConfirm(String id_user, String id_round_test, String confirm) {
    return candidateRepository.findByIdUserConfirm(id_user, id_round_test, confirm);
  }

  public Integer countByIdRoundAndIdGroup(String id_group_test, String id_round_test) {
    return candidateRepository.countByIdRoundAndIdGroup(id_group_test, id_round_test);
  }

  public Integer requestUser(String iduser, String idRound) {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String date = dateFormat.format(java.util.Calendar.getInstance().getTime());
    Integer counSave = 0;
    Integer count = candidateRepository.findByIdUser(iduser, idRound);
    if (count == 0) {
      candidateRepository.saveUser(date, "1", date, idRound, iduser, "0", "0", "1");
      counSave++;
    }
    if (counSave == 0) {
      return 1;
    } else {
      return 0;
    }
  }

  public int checkCompetitonChapterComplete(Long idu, Long idround) {
    String hql =
        "select count(*) from Candidate as c where c.user.id=:idu and c.counttest!=0 and c.roundTest.id=:idr";
    int soluong =
        Integer.parseInt(
            entityManager
                .createQuery(hql)
                .setParameter("idu", idu)
                .setParameter("idr", idround)
                .getSingleResult()
                .toString());
    return soluong;
  }

  public Integer countByRoundTestIdAndUserIdAndCounttest(
      Long idRoundTest, Long idUser, Integer counttest) {
    if (candidateRepository.countByRoundTestIdAndUserIdAndCounttest(idRoundTest, idUser, counttest)
        != 0) {
      return 1;
    }
    return 0;
  }

  public Integer saveCandicates(Long iduser, Long idRound, Integer counttest) {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String date = dateFormat.format(java.util.Calendar.getInstance().getTime());
    candidateRepository.saveUser(
        date, "1", date, idRound + "", iduser + "", counttest + "", "0", "0");
    return 0;
  }

  public void UpdateConfirmCandicates(Long iduser, Long idRound) {
    candidateRepository.updateConfirmByIduser(iduser + "", 0, idRound);
  }

  public void updateTimeStart(Date timeStart, Long iduser, Long idRound, Integer counttest) {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String date = dateFormat.format(timeStart);
    candidateRepository.updateTimeStart(date, iduser, idRound, counttest);
  }

  public void updateCountTest(Date timeStart, Long iduser, Long idRound, String countTest) {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String date = dateFormat.format(timeStart);
    candidateRepository.updateCountTestByUser(countTest, date, iduser, idRound);
  }

  public Candidate save(Candidate entity) {
    return candidateRepository.save(entity);
  }

  public List<Candidate> getListByIdUser(Long id, Long id2) {
    String hqlString = "from Candidate c where c.user.id=:cid and c.roundTest.id=:cidd";
    List<Candidate> list =
        entityManager
            .createQuery(hqlString)
            .setParameter("cid", id)
            .setParameter("cidd", id2)
            .getResultList();
    return list;
  }

  public int countByIdusser(Long id, Long idr) {
    int k = 0;
    int m = 0;
    String hqlString = "from Candidate c where c.user.id=:cid and c.roundTest.id=:idr";
    List<Candidate> list =
        entityManager
            .createQuery(hqlString)
            .setParameter("idr", idr)
            .setParameter("cid", id)
            .getResultList();
    for (Candidate c : list) {
      if (c.getCounttest() == 0) k++;
      else {
        m++;
      }
    }

    if (k == 1) return 0;
    if (m != 0) return m;
    return 0;
  }

  public CandidateDTO findByIdUserIdRoundTestCountTest(
      String id_user, String id_round_test, String countTest) {
    return candidateConverter.convertToDTO(
        candidateRepository.findByIdUserIdRoundTestCountTest(id_user, id_round_test, countTest));
  }

  public boolean isCurrentUserInRoundTest(Long roundTestId) throws Exception {
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (user.getId() == null) throw new Exception("You are not Login");
    return (candidateRepository.existsByUserInCandidates(user.getId(), roundTestId) > 0);
  }

  public void deleteByError(Long idUser, Long idRound, Integer count) {
    candidateRepository.deleteByErrorByIdUser(idUser);
    candidateRepository.deleteByErrorByIdUserTimeStartNotNull(idUser);
    resultRepository.deleteByErrorByIdUserIdRoundCount(idUser, idRound, count);
  }

  public List<CandidateDTO> findByidUserIdRound(Long idUser, Long idRound) {
    return candidateRepository.findByUserIdAndRoundTestId(idUser, idRound).stream()
        .map(item -> candidateConverter.convertToDTO(item))
        .collect(Collectors.toList());
  }




}
