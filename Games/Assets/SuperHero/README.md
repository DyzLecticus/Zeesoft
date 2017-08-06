Superhero
=========
This project is about learning how to use Blender to create game assets.
The result of this project will be a complete low polygon superhero character that can be imported and used in JMonkeyEngine.
  
Many Blender tutorials about textures refer to Photoshop.
[Gimp](https://www.gimp.org/) is a free alternative that was used to create the texture for this asset.
  
Steps
-----
 * Modelling  
   Create a mesh that represents the basic shape of the asset  
 * Texturing  
   Create the texture image that is wrapped around the asset mesh  
 * Rigging  
   Create the skeleton that can be used to animate the asset mesh  
 * Animation  
   Create named animations for specific actions like walking, running etc.  
 
Lessons learned
---------------
 * Think about the initial position of the creature with regard to baking the initial texture.  
   It may be better to spread out arms and legs a little to limit the shading that the baking procedure adds.
   This can be corrected later by recreating the texture.
 * Include seams in modelling process and regularly check the texture unwrapping for model errors.
 * Follow the natural anatomy when placing bones during the rigging phase.
 * Do not use inverse kinetics or postional constraints because the animations render differently in JMonkeyEngine compared to Blender.
 * When weight painting the mesh, make sure that no two bones fully control the same vertices because this will mess up rendering in JMonkeyEngine.
 * Check the rendering and game play of basic animations like "Idle", "Walk" and "Attack" before creating all other animations.
  
Tutorials
---------
 * [Interface controls](https://www.youtube.com/watch?v=iO5QHmBv4BU)
 * [Character modelling](https://www.youtube.com/watch?v=DiIoWrOlIRw)
 * [Character texturing](https://www.youtube.com/watch?v=JYBPXTful2g)
 * [Character rigging part 1](https://www.youtube.com/watch?v=Q9f-WVs3ghI)
 * [Character rigging part 2](https://www.youtube.com/watch?v=TPEmonfLo94)
 * [Character walk cycle](https://www.youtube.com/watch?v=DuUWxUitJos)
 * [Character run cycle](https://www.youtube.com/watch?v=_YdA-J27YPU)
  
Control cheat sheet
-------------------
 * Numpad1 = Front view
 * Numpad3 = Side view
 * Shift+c = Center cursor
 * Shift+a = Add
 * z = Toggle wire frame mode
 * x = Delete
 * Tab = Edit mode
 * Ctrl+r = loop cut
 * t = Show/hide tool window
 * Ctrl+e = edit (mark/clear seam)
 * a+a = select all
 * u+u = unwrap
 * Shift+Control+Alt+c / Origin to 3D cursor
