type Props = {};
export default async function ExamplePage({}: Props) {
  //top level await in the page(loading.tsx will be shown until the await finish)
  const res = await fetch("");

  /*
                  For even better experience use the use partial rendering
                  -> pass the promises to the components and wrap the component in react suspense boundary
                  -> and await promises inside the components inside use()
                  -> by this way you can load multiple components inside a page
                       */

  return <div>ExamplePage</div>;
}