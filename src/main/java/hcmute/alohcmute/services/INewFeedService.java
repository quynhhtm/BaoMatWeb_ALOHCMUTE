package hcmute.alohcmute.services;

import java.util.List;

import hcmute.alohcmute.entities.BaiViet;

public interface INewFeedService {
    List<BaiViet> findAll();
    List<BaiViet> findPublicOrFollowedPosts(String currentUsername);
	boolean toggleLike(int postId, String username);
	Boolean checkIfLiked(int maBaiViet, String currentUsername);
	int getLikeCount(int maBaiViet);
	int getCommentCount(int maBaiViet);
}