import { axiosApi } from "../util/commons";
import sea1 from "../assets/images/refact/sea1.jpg";
import profile1 from "../assets/images/refact/profile1.jpg";
import profile2 from "../assets/images/refact/profile2.jpg";
import profile3 from "../assets/images/refact/profile3.jpg";
import profile4 from "../assets/images/refact/profile4.jpg";
import profile5 from "../assets/images/refact/profile5.png";
import profile6 from "../assets/images/refact/profile6.jpg";

const api = axiosApi();

const url = "/boards";

async function getBoardList(type, fish, hash, sort) {
  return {
    boards: [
      {
        nickName: "슬픈 오징어",
        profileImageUrl: profile1,
        boardId: 1,
        boardImageUrl: sea1,
        hashtags: ["사진찍기 좋은", "연인과 함께"],
        content: "여수 낮바다~ 이 바람에 걸린 알 수 없는 향기가 있어~ 여수 낮바다도 너무 이쁘네여~ 여수김밥 먹었는데 꼭 드세여~~ 완전 힐링~",
        createdAt: "2024-01-25T21:58:14",
        commentCnt: 2,
        likeCnt: 1,
      },
      {
        nickName: "친절한 가자미",
        profileImageUrl: profile2,
        boardId: 2,
        boardImageUrl: "돌돔5짜.png",
        hashtags: [],
        content: "잡은 물고기 게시글",
        createdAt: "2024-01-26T21:59:20",
        commentCnt: 0,
        likeCnt: 0,
      },
      {
        nickName: "예쁜 갈치",
        profileImageUrl: profile3,
        boardId: 3,
        boardImageUrl: "돌돔5짜.png",
        hashtags: [],
        content: "잡은 물고기 게시글",
        createdAt: "2024-01-26T21:59:20",
        commentCnt: 0,
        likeCnt: 0,
      },
      {
        nickName: "아름다운 돌돔",
        profileImageUrl: profile4,
        boardId: 4,
        boardImageUrl: "돌돔5짜.png",
        hashtags: [],
        content: "잡은 물고기 게시글",
        createdAt: "2024-01-26T21:59:20",
        commentCnt: 0,
        likeCnt: 0,
      },
      {
        nickName: "감성적인 감성돔",
        profileImageUrl: profile5,
        boardId: 5,
        boardImageUrl: "돌돔5짜.png",
        hashtags: [],
        content: "잡은 물고기 게시글",
        createdAt: "2024-01-26T21:59:20",
        commentCnt: 0,
        likeCnt: 0,
      },
      {
        nickName: "귀여운 달고기",
        profileImageUrl: profile6,
        boardId: 6,
        boardImageUrl: "돌돔5짜.png",
        hashtags: [],
        content: "잡은 물고기 게시글",
        createdAt: "2024-01-26T21:59:20",
        commentCnt: 0,
        likeCnt: 0,
      },
    ],
    hasNext: false,
  };
  // try {
  //   console.log(type);
  //   const { data } = await api.get(
  //     `${url}?categoryId=${type}&fishBookId=${fish}&hashTagId=${hash}&sortType=${sort}`
  //   );
  //   console.log(data);
  //   return data;
  // } catch (error) {
  //   console.log(error);
  // }
}

async function getBoardDetail(id) {
  try {
    console.log(id);
    const { data } = await api.get(`${url}/${id}`);
    console.log(data);
    return data;
  } catch (error) {
    console.log(error);
  }
}

async function deleteBoardPost(id) {
  try {
    await api.delete(`${url}/${id}`);
  } catch (error) {
    console.log(error);
  }
}

async function modifyBoardPost(id) {
  try {
    await api.put(`${url}/${id}`);
  } catch (error) {
    console.log(error);
  }
}

async function createBoardPost(file) {
  console.log(JSON.stringify(file));
  try {
    await api.post(`${url}`, JSON.stringify(file));
  } catch (error) {
    console.log(error);
  }
}

async function modifyBoardPut(file, pageId) {
  console.log("pageId : ", pageId);
  console.log(JSON.stringify(file));
  try {
    await api.put(`${url}/${pageId}`, JSON.stringify(file));
  } catch (error) {
    console.log(error);
  }
}

async function sendBoardReply(id, reply) {
  try {
    await api.post(`${url}/${id}/comments`, { content: reply });
  } catch (error) {
    console.log(error);
  }
}

async function deleteBoardReply(id, replyId) {
  try {
    await api.delete(`${url}/${id}/comments/${replyId}`);
  } catch (error) {
    console.log(error);
  }
}

async function putPostLike(boardId) {
  try {
    await api.put(`${url}/${boardId}/likes`);
  } catch (error) {
    console.log(error);
  }
}

async function deletePostLike(boardId) {
  try {
    await api.delete(`${url}/${boardId}/likes`);
  } catch (error) {
    console.log(error);
  }
}

async function getSearchFish(word) {
  try {
    if (word == "") return;
    const { data } = await api.get(`${url}/search/fishBook?searchWord=${word}`);
    return data;
  } catch (error) {
    console.log(error);
  }
}

async function getSearchHash(word) {
  try {
    if (word == "") return;
    const { data } = await api.get(`${url}/search?searchWord=${word}`);
    return data;
  } catch (error) {
    console.log(error);
  }
}

async function getFilterFish() {
  // try {
  //   const { data } = await api.get(`${url}/filters`);
  //   return data;
  // } catch (error) {
  //   console.log(error);
  // }
  return {
    sea: [
      {
        id: 2,
        name: "바다1",
      },
      {
        id: 3,
        name: "감성돔",
      },
      {
        id: 4,
        name: "주꾸미",
      },
    ],
    freshWater: [
      {
        id: 1,
        name: "가물치",
      },
    ],
  };
}

async function getFilterHash() {
  try {
    const { data } = await api.get(`${url}/hashtags`);
    return data;
  } catch (error) {
    console.log(error);
  }
}

export {
  getBoardList,
  getBoardDetail,
  deleteBoardPost,
  modifyBoardPost,
  sendBoardReply,
  deleteBoardReply,
  putPostLike,
  deletePostLike,
  getSearchHash,
  getSearchFish,
  getFilterFish,
  getFilterHash,
  createBoardPost,
  modifyBoardPut,
};
