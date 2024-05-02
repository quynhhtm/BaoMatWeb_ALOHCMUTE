package hcmute.alohcmute.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hcmute.alohcmute.entities.BaiViet;
import hcmute.alohcmute.entities.TaiKhoan;
import hcmute.alohcmute.repositories.BaiVietRepository;
import hcmute.alohcmute.repositories.TaiKhoanRepository;

@Service
public class NewFeedServiceImpl implements INewFeedService {

	@Autowired
	BaiVietRepository baiVietRepository;

	@Autowired
	private TaiKhoanRepository taiKhoanRepository;

	public NewFeedServiceImpl(BaiVietRepository baiVietRepository) {
		this.baiVietRepository = baiVietRepository;
	}

	@Override
	public List<BaiViet> findAll() {
		return baiVietRepository.findAll();
	}

	@Autowired
	ITaiKhoanService taiKhoanService;

	// Other existing methods...
	@Override
	public List<BaiViet> findPublicOrFollowedPosts(String currentUsername) {
		// Get the list of TaiKhoan entities that the current user follows
		List<TaiKhoan> followedUsers = taiKhoanService.findTaiKhoanTheoDoisByUsername(currentUsername);
		System.out.println(followedUsers.size());
		List<BaiViet> allPosts = baiVietRepository.findAll();
		List<BaiViet> eligiblePosts = new ArrayList<>();

		for (BaiViet baiViet : allPosts) {
			if (baiViet.isEnable()) {
				if ((baiViet.getCheDoNhom().getTenCheDo().equals("Công khai")
						|| (baiViet.getCheDoNhom().getTenCheDo().equals("Người theo dõi")
								&& followedUsers.contains(baiViet.getTaiKhoan()))
						|| baiViet.getTaiKhoan().getTaiKhoan().equals(currentUsername)) && baiViet.getNhom() == null) {
					eligiblePosts.add(baiViet);
				} else if ((baiViet.getNhom() != null
						&& baiVietRepository.isUserInGroup(currentUsername, baiViet.getNhom().getMaNhom()))) {
					eligiblePosts.add(baiViet);
				}
			}
		}

		// Sort eligible posts by date and time in descending order
		List<BaiViet> sortedPosts = eligiblePosts.stream().sorted((bv1, bv2) -> {
			int dateCompare = bv2.getNgay().compareTo(bv1.getNgay());
			if (dateCompare == 0) {
				return bv2.getThoiGian().compareTo(bv1.getThoiGian());
			}
			return dateCompare;
		}).collect(Collectors.toList());

		// Extract and shuffle the top 20 recent posts
		List<BaiViet> top20Posts = new ArrayList<>(sortedPosts.stream().limit(20).collect(Collectors.toList()));
		Collections.shuffle(top20Posts);

		// Remove the original top 20 posts from the sorted list
		sortedPosts.subList(0, Math.min(20, sortedPosts.size())).clear();

		// Add the shuffled top 20 posts back to the sorted list
		sortedPosts.addAll(0, top20Posts);

		return sortedPosts;

	}

	@Override
	@Transactional
	public boolean toggleLike(int postId, String username) {
		Optional<BaiViet> baiVietOpt = baiVietRepository.findById(postId);
		TaiKhoan taiKhoan = taiKhoanRepository.findOneBytaiKhoan(username);

		if (baiVietOpt.isPresent() && taiKhoan != null) {
			BaiViet baiViet = baiVietOpt.get();

			Set<TaiKhoan> likes = baiViet.getTaiKhoans();
			boolean isLiked = likes.contains(taiKhoan);
			if (isLiked) {
				likes.remove(taiKhoan);
				taiKhoan.getBaiVietTuongTacs().remove(baiViet);
			} else {
				likes.add(taiKhoan);
				taiKhoan.getBaiVietTuongTacs().add(baiViet);
			}

			// Cập nhật cả hai thực thể để đảm bảo đồng bộ trên cả hai phía của mối quan hệ
			// Many-to-Many.
			baiVietRepository.save(baiViet);
			taiKhoanRepository.save(taiKhoan); // Bạn cần đảm bảo rằng mối quan hệ từ phía TaiKhoan cũng được cập nhật.

			return !isLiked;
		}

		return false;
	}

	@Override
	public Boolean checkIfLiked(int maBaiViet, String username) {
		TaiKhoan taiKhoan = taiKhoanRepository.findOneBytaiKhoan(username);
		Optional<BaiViet> optionalBaiViet = baiVietRepository.findById(maBaiViet);

		if (taiKhoan != null && optionalBaiViet.isPresent()) {
			BaiViet baiViet = optionalBaiViet.get(); // Lấy ra đối tượng BaiViet
			return taiKhoan.getBaiVietTuongTacs().contains(baiViet);
		}

		return false;
	}

	@Override
	public int getLikeCount(int maBaiViet) {
		return baiVietRepository.countLikesByBaiVietId(maBaiViet);
	}

	@Override
	public int getCommentCount(int maBaiViet) {
		return baiVietRepository.countCommentsByBaiVietId(maBaiViet);
	}

}
