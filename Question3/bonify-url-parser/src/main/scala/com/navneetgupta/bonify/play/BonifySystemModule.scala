package com.navneetgupta.bonify.play

import play.api.mvc.ControllerComponents

trait BonifySystemModule {
  def controllerComponents: ControllerComponents
}