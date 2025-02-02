import React, { useState } from "react";
import { useLocation } from "react-router-dom";
import BoardFormItem from "./BoardFormItem";
import Header from "../Header";

const BoardCreate = () => {
  const location = useLocation();

  const [categoryId, setCategoryId] = useState(location.state?.categoryId);

  return (
    <>
      <Header centerText="글쓰기" />
      <div>
        <BoardFormItem type={"create"} categoryId={categoryId} />
      </div>
    </>
  );
};

export default BoardCreate;
