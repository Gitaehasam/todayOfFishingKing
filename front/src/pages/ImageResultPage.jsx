import { useEffect, useRef, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import * as tmImage from "@teachablemachine/image";
import "../assets/styles/fishrecognition/ImageResultPage.scss";
import { IoIosArrowBack, IoIosInformationCircleOutline } from "react-icons/io";
import Header from "../components/Header";
import Loading from "../components/Loading";
import InfoOutlinedIcon from "@mui/icons-material/InfoOutlined";

const URL = "/my_model/";

const ImageResultPage = () => {
  const location = useLocation();
  const result = location.state.value;
  const imgRef = useRef(null);
  const modelRef = useRef(null);
  const [name, setName] = useState(null);
  const [fishDatas, setFishDatats] = useState([]);
  const [getInfo, setGetInfo] = useState(false);

  const predict = async () => {
    console.log(modelRef.current);
    console.log(imgRef.current);
    if (modelRef.current && imgRef.current) {
      try {
        let image = imgRef.current;
        const prediction = await modelRef.current.predict(image, false);
        prediction.sort(
          (a, b) => parseFloat(b.probability) - parseFloat(a.probability)
        );
        const classPrediction = prediction[0].className;
        console.log(classPrediction);
        setName(classPrediction);
      } catch (err) {
        console.error("Prediction error:", err);
      }
    }
  };

  const loadModel = async () => {
    try {
      const modelURL = URL + "model.json";
      const metadataURL = URL + "metadata.json";
      const model = await tmImage.load(modelURL, metadataURL);
      modelRef.current = model;
      await predict();
    } catch (err) {
      console.error("Model loading error:", err);
    }
  };

  useEffect(() => {
    loadModel();

    return () => {
      imgRef.current = null;
    };
  }, []);

  // if (!name) {
  //   return <Loading />;
  // }

  return (
    <>
      <div className="result" style={{ display: !name && "none" }}>
        <Header centerText={"분석완료"} align="center" />
        {/* <InfoOutlinedIcon
          style={{ width: "6vh", height: "3.5vh" }}
          onClick={() => setGetInfo(true)}
        />
        {getInfo && (
          <>
            <div className="get-info" onClick={() => setGetInfo(false)}></div>
            <div className="result-modal">1</div>
          </>
        )} */}
        <div className="result-body">
          <div
            className="result-img"
            // style={{ display: name ? "flex" : "none" }}
          >
            <img className="ml-result" src={result} ref={imgRef} alt="" />
          </div>
          <div className="result-content">
            <div className="result-fish">
              <div className="result-name">{name}</div>
              <div className="result-detail">자세히 보기</div>
            </div>
            {/* <div className="result-detail">도감보러가기</div> */}
            <div className="fish-reviews">
              <h3 className="reviews-title">{name}의 리뷰</h3>
              <div className="wrapper">
                {fishDatas.length ? (
                  fishDatas.map((review) => {
                    return (
                      <div>
                        <img
                          key={review}
                          src="https://cdn.iconsumer.or.kr/news/photo/201806/7349_8772_1719.jpg"
                        />
                        <div>{review}</div>
                      </div>
                    );
                  })
                ) : (
                  <div className="none-data">정보를 준비 중입니다.</div>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
      <Loading hidden={name} />
    </>
  );
};

export default ImageResultPage;
