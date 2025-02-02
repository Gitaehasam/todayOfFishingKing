/*
Auto-generated by: https://github.com/pmndrs/gltfjsx
Command: npx gltfjsx@6.2.16 ing.glb 
Author: ffish.asia / floraZia.com (https://sketchfab.com/ffishAsia-and-floraZia)
License: CC-BY-4.0 (http://creativecommons.org/licenses/by/4.0/)
Source: https://sketchfab.com/3d-models/crucian-carp-carassius-auratus-langsdorfii-ccd3144068d34cd9b15f3c38d2fb790b
Title: ギンブナ Crucian Carp, Carassius auratus langsdorfii
*/

import React, { useRef, useEffect } from "react";
import { useFrame } from "@react-three/fiber";
import { useGLTF } from "@react-three/drei";
import * as THREE from "three";
import { TweenMax, Power1 } from "gsap";

export function IngFish(props) {
  const { nodes, materials } = useGLTF("/ing.glb");

  const fishGroupRef = useRef();
  const fishMovement = useRef({
    speed: 0.02,
    targetPosition: new THREE.Vector3(), // Target position for smooth movement
  });

  useEffect(() => {
    // Set initial target position
    updateTargetPosition();
  }, []);

  useFrame((state, delta) => {
    if (fishGroupRef.current) {
      // 부드러운 이동
      fishGroupRef.current.position.lerp(fishMovement.current.targetPosition, delta * 0.5);

      // 부드러운 회전
      const lookAtVector = new THREE.Vector3().copy(fishMovement.current.targetPosition).sub(fishGroupRef.current.position);
      fishGroupRef.current.rotation.y = Math.atan2(lookAtVector.x, lookAtVector.z);

      // 일정 시간이 지날 때마다 새로운 목표 위치 설정
      if (state.clock.elapsedTime % 5 < delta) {
        updateTargetPosition();
      }
    }
  });

  const updateTargetPosition = () => {
    fishMovement.current.targetPosition.set(Math.random() * 10 - 2, Math.random() * 2 - 10, Math.random() * 10 - 5);
    TweenMax.to(fishMovement.current.targetPosition, 5, {
      x: Math.random() * 10 - 5,
      y: Math.random() * 2 - 10,
      z: Math.random() * 10 - 5,
      ease: Power1.easeInOut,
    });
  };

  return (
    <group {...props} ref={fishGroupRef} dispose={null}>
      <group rotation={[2.148, 1.409, 2.097]} scale={0.3}>
        <mesh geometry={nodes.Object_6.geometry} material={materials["Q10790-1all-fill"]} />
        <mesh geometry={nodes.Object_7.geometry} material={materials["Q10790-1all-fill"]} />
        <mesh geometry={nodes.Object_8.geometry} material={materials["Q10790-1all-fill"]} />
        <mesh geometry={nodes.Object_9.geometry} material={materials["Q10790-1all-fill"]} />
        <mesh geometry={nodes.Object_10.geometry} material={materials["Q10790-1all-fill"]} />
        <mesh geometry={nodes.Object_11.geometry} material={materials["Q10790-1all-fill"]} />
      </group>
    </group>
  );
}

useGLTF.preload("/ing.glb");
