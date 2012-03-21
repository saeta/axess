// Copyright 2012 Brennan Saeta
//
// This file is part of Axess
//
// Axess is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Axess is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with Axess.  If not, see <http://www.gnu.org/licenses/>.

package models

case class SysStats(heap: Long, maxHeap: Long,
  load: Double, procs: Long,
  fsTotal: Long, fsFree: Long) {

  def fsPercent = ((fsTotal - fsFree).toDouble / fsTotal.toDouble) * 100
  def fsDanger = fsPercent > 90
  def fsWarn = fsPercent > 80
  def heapPercent = (heap.toDouble / maxHeap.toDouble) * 100
  def heapDanger = heapPercent > 85
  def heapWarn = heapPercent > 70
  def loadPercent = math.min((load / procs.toDouble) * 100, 100)
  def loadDanger = loadPercent >= 100
  def loadWarn = loadPercent > 80
}

object SysStats {
  import java.lang.management.ManagementFactory
  import java.lang.management.OperatingSystemMXBean
  import java.io.File

  def get() = {
    val rt = Runtime.getRuntime()
    val osb = ManagementFactory.getOperatingSystemMXBean()
    val f = new File(".")
    SysStats(rt.totalMemory(),
      rt.maxMemory(),
      osb.getSystemLoadAverage(),
      osb.getAvailableProcessors(),
      f.getTotalSpace(),
      f.getUsableSpace())
  }
}
